package com.pvt152.StudentLoppet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.pvt152.StudentLoppet.model.RunnerResult;
import com.pvt152.StudentLoppet.repository.MidnattsloppResultRepository;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UserService;
import com.pvt152.StudentLoppet.repository.UserRepository;
import com.pvt152.StudentLoppet.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultScraper {

    @Autowired
    private MidnattsloppResultRepository resultRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityService activityService;

    // scheduled run : 00:00 - August 25 / 2024
    @Scheduled(cron = "0 0 0 25 8 ?", zone = "Europe/Stockholm")
    public void scrapeAllClasses() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Stockholm"));
        if (now.toLocalDate().equals(LocalDate.of(2024, 8, 25))) {
            List<String> classIds = fetchClassIdsFor2023();
            for (String classId : classIds) {
                scrapeClass(classId);
            }
            System.out.println("Scraping all classes completed.");
        } else {
            System.out.println("Today is not May 23, 2024. Skipping the scraping task.");
        }
    }

    public List<String> fetchClassIdsFor2023() {
        List<String> classIds = new ArrayList<>();
        String url = "https://history.midnattsloppet.com/?EventGroupId=96&Year=2023";

        try {
            Document doc = Jsoup.connect(url).get();
            Elements options = doc.select("select#ClassId option");

            for (Element option : options) {
                String value = option.attr("value");
                if (!value.isEmpty()) {
                    classIds.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fetched ClassIds for 2023: " + classIds);
        return classIds;
    }

    public void scrapeClass(String classId) {
        int pageNo = 1;
        boolean morePages = true;

        while (morePages) {
            String url = "https://history.midnattsloppet.com/?EventGroupId=96&Year=2023&ClassId=" + classId
                    + "&FirstName=&LastName=&NationalityCountryId=&Club=&BirthYearFrom=&BirthYearTo=&PlaceFrom=&Search=Search+Results&PageNo="
                    + pageNo;

            try {
                System.out.println("Scraping URL: " + url);
                Document doc = Jsoup.connect(url).get();
                boolean runnersExtracted = extractRunners(doc);

                if (runnersExtracted) {
                    pageNo++;
                } else {
                    morePages = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                morePages = false;
            }
        }
    }

    private boolean extractRunners(Document doc) {
        String raceClass = extractRaceClass(doc);
        System.out.println("Extracted race class: " + raceClass);

        double distance = extractDistanceFromRaceClass(raceClass);

        Elements rows = doc.select("div.row.border-bottom");
        System.out.println("Number of rows found: " + rows.size());

        if (rows.isEmpty()) {
            return false;
        }

        for (Element row : rows) {
            try {
                String placeStr = row.select("div.col-12.col-sm-6").text().split("\\.")[0].trim();
                int place = Integer.parseInt(placeStr);

                String id = row.select("a.history").attr("data-id");
                String name = row.select("a.history").text();
                String organization = row.select("div.col-9.col-sm-4").text();
                String time = extractTime(row);

                int duration = convertTimeToMinutes(time);

                String yearOfBirthStr = row.select("div.col-12.col-sm-6").text();
                int yearOfBirth = extractYearOfBirth(yearOfBirthStr, name);

                System.out.println("Extracted runner - ID: " + id + ", Name: " + name + ", Organization: "
                        + organization + ", Time: " + time + ", Year of Birth: " + yearOfBirth + ", Place: " + place);

                RunnerResult runner = new RunnerResult(id, name, organization, time, raceClass, yearOfBirth, place);

                if (!resultRepository.existsById(id)) {
                    resultRepository.save(runner);
                    System.out.println("Saved runner: " + name);
                    checkAndIncreaseUserScore(runner, distance, duration); // Updated to pass distance and duration
                } else {
                    System.out.println("Runner already exists: " + name);
                }
            } catch (Exception e) {
                System.out.println("Error processing row: " + row.text());
                e.printStackTrace();
            }
        }
        return true;
    }

    private void checkAndIncreaseUserScore(RunnerResult runner, double distance, int duration) {
        if (userRepository.existsByFullNameAndYearOfBirth(runner.getName(), runner.getYearOfBirth())) {
            User user = userRepository.findByFullNameAndYearOfBirth(runner.getName(), runner.getYearOfBirth());
            int score = activityService.calculateScore(distance, duration) * 10;
            userService.increaseScore(user.getEmail(), score);
            System.out.println("Increased score for user: " + user.getEmail() + " by " + score);
        }
    }

    private String extractRaceClass(Document doc) {
        Element raceClassElement = doc.selectFirst("div.col-12:contains(Midnattsloppet Stockholm 2023)");
        if (raceClassElement != null) {
            String raceClassText = raceClassElement.text();
            return raceClassText;
        }
        return "Unknown";
    }

    private double extractDistanceFromRaceClass(String raceClass) {
        if (raceClass.contains("10 km")) {
            return 10.0;
        } else if (raceClass.contains("5 km")) {
            return 5.0;
        }
        return 0.0;
    }

    private String extractTime(Element row) {
        Element timeElement = row.selectFirst("a.splittime");
        if (timeElement != null) {
            return timeElement.text();
        } else {
            String timeText = row.select("div.col-3.col-sm-2").text();
            if (timeText.matches("\\d{2}:\\d{2}:\\d{2}")) {
                return timeText;
            }
        }
        return "";
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return hours * 60 + minutes + seconds / 60;
    }

    private int extractYearOfBirth(String text, String name) {
        String[] parts = text.split(name);
        if (parts.length > 0) {
            String partBeforeName = parts[0];
            String yearStr = extractYearString(partBeforeName);
            if (yearStr != null) {
                return Integer.parseInt(yearStr);
            }
        }

        if (parts.length > 1) {
            String partAfterName = parts[1];
            String yearStr = extractYearString(partAfterName);
            if (yearStr != null) {
                return Integer.parseInt(yearStr);
            }
        }

        return 0;
    }

    private String extractYearString(String part) {
        part = part.replaceAll("\\D+", "");
        if (!part.isEmpty() && part.length() == 2) {
            return part;
        }
        return null;
    }
}