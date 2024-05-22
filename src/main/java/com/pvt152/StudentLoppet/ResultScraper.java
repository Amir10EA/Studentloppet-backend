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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class ResultScraper {

    @Autowired
    private MidnattsloppResultRepository resultRepository;

    // This method will be scheduled to run daily at 17:50
    @Scheduled(cron = "0 57 17 * * *", zone = "Europe/Stockholm")
    public void scrapeAllClasses() {
        // Check if the current date is May 22, 2024
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Stockholm"));
        if (now.toLocalDate().equals(LocalDate.of(2024, 5, 22))) {
            List<String> classIds = Arrays.asList("20003125", "10003125", "20003127", "10003127");
            for (String classId : classIds) {
                scrapeClass(classId);
            }
            System.out.println("Scraping all classes completed.");
        } else {
            System.out.println("Today is not May 22, 2024. Skipping the scraping task.");
        }
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

        Elements rows = doc.select("div.row.border-bottom");
        System.out.println("Number of rows found: " + rows.size());

        if (rows.isEmpty()) {
            return false; // No more rows to process
        }

        for (Element row : rows) {
            try {
                String placeStr = row.select("div.col-12.col-sm-6").text().split("\\.")[0].trim();
                int place = Integer.parseInt(placeStr);

                String id = row.select("a.history").attr("data-id");
                String name = row.select("a.history").text();
                String organization = row.select("div.col-9.col-sm-4").text();
                String time = extractTime(row);

                String yearOfBirthStr = row.select("div.col-12.col-sm-6").text();
                int yearOfBirth = extractYearOfBirth(yearOfBirthStr, name);

                System.out.println("Extracted runner - ID: " + id + ", Name: " + name + ", Organization: "
                        + organization + ", Time: " + time + ", Year of Birth: " + yearOfBirth + ", Place: " + place);

                RunnerResult runner = new RunnerResult(id, name, organization, time, raceClass, yearOfBirth, place);

                if (!resultRepository.existsById(id)) {
                    resultRepository.save(runner);
                    System.out.println("Saved runner: " + name);
                } else {
                    System.out.println("Runner already exists: " + name);
                }
            } catch (Exception e) {
                System.out.println("Error processing row: " + row.text());
                e.printStackTrace();
            }
        }
        return true; // Rows were processed successfully
    }

    private String extractRaceClass(Document doc) {
        Element raceClassElement = doc.selectFirst("div.col-12:contains(Midnattsloppet Stockholm 2023)");
        if (raceClassElement != null) {
            String raceClassText = raceClassElement.text();
            return raceClassText; // Return the full race class description
        }
        return "Unknown"; // Default value in case extraction fails
    }

    private String extractTime(Element row) {
        Element timeElement = row.selectFirst("a.splittime");
        if (timeElement != null) {
            return timeElement.text();
        } else {
            // If the time is not in an <a> tag, extract it directly from the div
            String timeText = row.select("div.col-3.col-sm-2").text();
            // Ensure the time is a valid format
            if (timeText.matches("\\d{2}:\\d{2}:\\d{2}")) {
                return timeText;
            }
        }
        return ""; // Return an empty string if time is not found
    }

    private int extractYearOfBirth(String text, String name) {
        // First, try to extract year from text before the runner's name
        String[] parts = text.split(name);
        if (parts.length > 0) {
            String partBeforeName = parts[0];
            String yearStr = extractYearString(partBeforeName);
            if (yearStr != null) {
                return Integer.parseInt(yearStr);
            }
        }

        // If not found, try to extract year from text after the runner's name
        if (parts.length > 1) {
            String partAfterName = parts[1];
            String yearStr = extractYearString(partAfterName);
            if (yearStr != null) {
                return Integer.parseInt(yearStr);
            }
        }

        return 0; // Return a default value or handle the error appropriately
    }

    private String extractYearString(String part) {
        part = part.replaceAll("\\D+", ""); // Remove non-numeric characters
        if (!part.isEmpty() && part.length() == 2) {
            return part; // Return if it is a valid two-digit year
        }
        return null; // Return null if no valid year found
    }
}