package com.pvt152.StudentLoppet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public Activity logActivity(String userEmail, double distance, long duration) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setDistance(distance);
        activity.setDuration(duration);
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
        return (int) (distance * 10);
    }

    public Map<String, Object> getTotalDistanceAndDuration(String userEmail) {
        List<Activity> activities = activityRepository.findByUserEmail(userEmail);
        int userScore = userRepository.findScoreByEmail(userEmail) != null ? userRepository.findScoreByEmail(userEmail)
                : 0;
        return calculateTotalDistanceAndDuration(activities, userScore);
    }

    public Map<String, Object> getTotalDistanceAndDurationPastWeek(String userEmail) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<Activity> activities = activityRepository.findByUserEmailAndTimestampAfter(userEmail, oneWeekAgo);
        int userScorePastWeek = activities.stream().mapToInt(a -> calculateScore(a.getDistance(), a.getDuration()))
                .sum();
        return calculateTotalDistanceAndDuration(activities, userScorePastWeek);
    }

    public Map<String, Object> getTotalDistanceAndDurationByUniversity(University university) {
        List<Activity> activities = activityRepository.findByUniversity(university);
        List<Object[]> universityScores = userRepository.findScoresByUniversity();
        int universityScore = 0;
        for (Object[] row : universityScores) {
            if (row[0] == university) {
                universityScore = ((Number) row[1]).intValue();
                break;
            }
        }
        Map<String, Object> summary = calculateTotalDistanceAndDuration(activities, universityScore);
        summary.put("universityName", university.getDisplayName()); // Add the university display name to the summary
        return summary;
    }

    private Map<String, Object> calculateTotalDistanceAndDuration(List<Activity> activities, int totalScore) {
        double totalDistance = activities.stream().mapToDouble(Activity::getDistance).sum(); // sammanlagd sprungen
                                                                                             // sträcka i km
        long totalDuration = activities.stream().mapToLong(Activity::getDuration).sum(); // sammanlagd sprungen tid i
                                                                                         // min

        double minPerKm = 0;
        if (totalDistance > 0) {
            minPerKm = totalDuration / totalDistance; // Minutes per kilometer
        }

        // OBS!!! Denna metod för at beräkna hur kalorier förbränns behöver
        // dubbelceckas!!!
        double caloriesBurned = totalDistance * 50;

        Map<String, Object> result = new HashMap<>();
        result.put("totalDistance", totalDistance);
        result.put("totalDuration", totalDuration);
        result.put("averageSpeed", minPerKm);
        result.put("caloriesBurned", caloriesBurned);
        result.put("totalScore", totalScore);

        return result;
    }
}
