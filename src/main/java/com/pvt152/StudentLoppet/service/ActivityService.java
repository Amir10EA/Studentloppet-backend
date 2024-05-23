package com.pvt152.StudentLoppet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.dto.UniversityMetricDTO;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UniversityService universityService;

    public Activity logActivity(String userEmail, double distance, double duration) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate and update score based on the activity
        int score = calculateScore(distance, duration);
        userService.increaseScore(userEmail, score);

        Activity activity = new Activity(distance, duration, user, score); // pass the score to the constructor
        activity.setTimestamp(LocalDateTime.now());
        activityRepository.save(activity);

        return activity;
    }

    public int calculateScore(double distance, double duration) {
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
        int universityRank = universityService.getUniversityRank(university);
        summary.put("universityRank", universityRank);

        return summary;
    }

    private Map<String, Object> calculateTotalDistanceAndDuration(List<Activity> activities, int totalScore) {
        double totalDistance = activities.stream().mapToDouble(Activity::getDistance).sum(); // Total distance run in km
        double totalDuration = activities.stream().mapToDouble(Activity::getDuration).sum(); // Total time run in min

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

}