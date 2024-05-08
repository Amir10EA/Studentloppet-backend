package com.pvt152.StudentLoppet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.dto.UniversityScoreDTO;
import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityLeaderboardService universityLeaderboardService;

    public Activity logActivity(String userEmail, double distance, long duration) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Activity activity = new Activity(distance, duration, user);
        activity.setTimestamp(LocalDateTime.now());
        activityRepository.save(activity);

        // Calculate and update score based on the activity
        int score = calculateScore(distance, duration);
        userService.increaseScore(userEmail, score);
        return activity;
    }

    // Ändra senare, hur många poäng ska en kilometer omvandlas till, just nu 1km =
    // 10poäng
    private int calculateScore(double distance, long duration) {
        if (duration == 0)
            return 0;
        double durationInHours = duration / 60.0;
        double speed = distance / durationInHours;
        return (int) (distance * 10 * speed);
    }

    public Map<String, Object> getTotalDistanceAndDuration(String userEmail) {
        List<Activity> activities = activityRepository.findByUserEmail(userEmail);
        int userScore = userRepository.findScoreByEmail(userEmail) != null ? userRepository.findScoreByEmail(userEmail)
                : 0;
        Map<String, Object> result = calculateTotalDistanceAndDuration(activities, userScore);

        // Get the rank of the user and add it to the result
        int userRank = userService.getUserRank(userEmail);
        result.put("userRank", userRank);

        return result;
    }

    public Map<String, Object> getTotalDistanceAndDurationPastWeek(String userEmail) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<Activity> activities = activityRepository.findByUserEmailAndTimestampAfter(userEmail, oneWeekAgo);
        int userScorePastWeek = activities.stream().mapToInt(a -> calculateScore(a.getDistance(), a.getDuration()))
                .sum();
        Map<String, Object> result = calculateTotalDistanceAndDuration(activities, userScorePastWeek);

        // Get the rank of the user based on the past week's activities and add it to
        // the result
        int userRankPastWeek = userService.getUserRank(userEmail); // Assuming rank is based on total score not just
                                                                   // past week
        result.put("userRank", userRankPastWeek);

        return result;
    }

    public Map<String, Object> getTotalDistanceAndDurationByUniversity(University university) {
        List<Activity> activities = activityRepository.findByUniversity(university);
        List<Object[]> universityScores = userRepository.findScoresByUniversity();

        // Fetch the number of users at the university
        int numberOfStudents = userRepository.countUsersByUniversity().stream()
                .filter(objects -> university.equals(objects[0]))
                .map(objects -> ((Long) objects[1]).intValue())
                .findFirst()
                .orElse(0);

        int universityScore = 0;
        for (Object[] row : universityScores) {
            if (row[0] == university) {
                universityScore = ((Number) row[1]).intValue();
                break;
            }
        }

        Map<String, Object> summary = calculateTotalDistanceAndDuration(activities, universityScore);
        summary.put("universityName", university.getDisplayName()); // Add the university display name to the summary
        summary.put("numberOfStudents", numberOfStudents); // Add number of students to the summary

        // Get the rank of the university and add it to the summary
        int universityRank = getUniversityRank(university);
        summary.put("universityRank", universityRank);

        return summary;
    }

    public List<UniversityScoreDTO> calculateUniversityScores() {
        return userRepository.findScoresByUniversity().stream()
                .map(result -> new UniversityScoreDTO(
                        ((University) result[0]).getDisplayName(),
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Object> calculateTotalDistanceAndDuration(List<Activity> activities, int totalScore) {
        double totalDistance = activities.stream().mapToDouble(Activity::getDistance).sum(); // Total distance run in km
        long totalDuration = activities.stream().mapToLong(Activity::getDuration).sum(); // Total time run in min

        double minPerKm = 0;
        if (totalDistance > 0) {
            minPerKm = totalDuration / totalDistance; // Minutes per kilometer
        }

        // Sum up the calories burned for all activities
        double caloriesBurned = activities.stream().mapToDouble(Activity::getCaloriesBurned).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalDistance", totalDistance);
        result.put("totalDuration", totalDuration);
        result.put("averageSpeed", minPerKm);
        result.put("caloriesBurned", caloriesBurned);
        result.put("totalScore", totalScore);

        return result;
    }

    public List<UserScoreDTO> getStudentsByScore(University university) {
        List<User> users = userRepository.findByUniversity(university);
        return users.stream()
                .map(user -> new UserScoreDTO(user.getFirstName() + " " + user.getLastName(), user.getEmail(),
                        user.getScore()))
                .sorted(Comparator.comparingInt(UserScoreDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

    public List<UserStats> getStudentsByDistance(University university) {
        List<Activity> activities = activityRepository.findByUniversity(university);
        return activities.stream()
                .collect(Collectors.groupingBy(Activity::getUser,
                        Collectors.summingDouble(Activity::getDistance)))
                .entrySet().stream()
                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(), e.getValue()))
                .sorted(Comparator.comparingDouble(UserStats::getValue).reversed())
                .collect(Collectors.toList());
    }

    public List<UserStats> getStudentsBySpeed(University university) {
        List<Activity> activities = activityRepository.findByUniversity(university);

        // räkna ut total sträcka och tid för varje användare/student
        Map<User, Double[]> totals = activities.stream()
                .collect(Collectors.toMap(
                        Activity::getUser,
                        a -> new Double[] { a.getDistance(), (double) a.getDuration() },
                        (totals1, totals2) -> new Double[] { totals1[0] + totals2[0], totals1[1] + totals2[1] }));

        // räkna ut min/km för varje användare/student
        Map<User, Double> userSpeeds = totals.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Double totalDistance = entry.getValue()[0];
                            Double totalDuration = entry.getValue()[1];
                            return totalDistance > 0 ? totalDuration / totalDistance : 0;
                        }));

        // Convert map to sorted list of UserStats
        return userSpeeds.entrySet().stream()
                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(), e.getValue()))
                .sorted(Comparator.comparingDouble(UserStats::getValue))
                .collect(Collectors.toList());
    }

    public List<UserStats> getStudentsByCaloriesBurned(University university) {
        List<Activity> activities = activityRepository.findByUniversity(university);
        return activities.stream()
                .collect(
                        Collectors.groupingBy(Activity::getUser, Collectors.summingDouble(Activity::getCaloriesBurned)))
                .entrySet().stream()
                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(), e.getValue()))
                .sorted(Comparator.comparingDouble(UserStats::getValue).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Double> sumDistanceByUniversity() {
        return activityRepository.sumDistanceByUniversity().stream()
                .collect(Collectors.toMap(
                        entry -> ((University) entry[0]).getDisplayName(),
                        entry -> ((Number) entry[1]).doubleValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public int getUniversityRank(University university) {
        List<UniversityScoreDTO> universityScores = calculateUniversityScores();
        int rank = 1;
        int actualRank = 1; // This holds the actual rank to be returned
        Integer lastScore = null; // Store the last score for comparison

        for (UniversityScoreDTO universityScore : universityScores) {
            if (lastScore == null || !lastScore.equals(universityScore.getScore())) {
                lastScore = universityScore.getScore();
                rank = actualRank;
            }
            if (universityScore.getUniversityDisplayName().equals(university.getDisplayName())) {
                return rank;
            }
            actualRank++;
        }
        return -1; // In case the university is not found
    }

}
