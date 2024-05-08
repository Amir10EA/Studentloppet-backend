package com.pvt152.StudentLoppet.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UserService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    // Denna endpoint används för att posta nya aktiviteter till databasen.
    @PostMapping(path = "/addActivity/{email}/{distance}/{duration}")
    public ResponseEntity<?> logActivity(@PathVariable String email,
            @PathVariable("distance") double distance,
            @PathVariable("duration") long duration) {
        try {
            if (Double.isNaN(distance) || duration < 0) {
                throw new IllegalArgumentException("Invalid distance or duration");
            }
            Activity activity = activityService.logActivity(email, distance, duration);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Denna endpoint används för få fram en sammanfattning över alla aktiviterer
    // från en användare (sammanlagd distance, duration, calories burned, avarage
    // speed (min/km), total score)
    @GetMapping(path = "/total/{email}")
    public ResponseEntity<?> getTotalDistanceAndDuration(@PathVariable String email) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDuration(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Denna endpoint används för få fram en sammanfattning över alla aktiviterer
    // från en användare för den senaste veckan (sammanlagd distance, duration,
    // calories burned, avarage speed (min/km), total score),
    // score räknar endast ut summan av alla aktiviteter från den senaste veckan,
    // när flera poänginsamlingssätt finns, updatera metoderna i servicen
    @GetMapping(path = "/totalWeekSummary/{email}")
    public ResponseEntity<?> getTotalDistanceAndDurationForWeek(@PathVariable String email) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDurationPastWeek(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Denna endpoint används för få fram en sammanfattning över alla aktiviterer
    // från ett universitets alla användare (sammanlagd distance, duration,
    // calories burned, avarage speed (min/km), total score)
    @GetMapping(path = "/totalUniversity/{university}")
    public ResponseEntity<?> getTotalDistanceAndDurationByUniversity(@PathVariable University university) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDurationByUniversity(university);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/sortedByScore/{university}")
    public ResponseEntity<List<UserScoreDTO>> sortedByScore(@PathVariable University university) {
        try {
            List<UserScoreDTO> userScores = activityService.getStudentsByScore(university);
            return ResponseEntity.ok(userScores);
        } catch (Exception e) {
            // Return an empty list and a bad request status if there is an error
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/sortedByDistance/{university}")
    public ResponseEntity<List<UserStats>> sortedByDistance(@PathVariable University university) {
        return ResponseEntity.ok(activityService.getStudentsByDistance(university));
    }

    @GetMapping("/sortedBySpeed/{university}")
    public ResponseEntity<List<UserStats>> sortedBySpeed(@PathVariable University university) {
        return ResponseEntity.ok(activityService.getStudentsBySpeed(university));
    }

    @GetMapping("/sortedByCalories/{university}")
    public ResponseEntity<List<UserStats>> sortedByCalories(@PathVariable University university) {
        return ResponseEntity.ok(activityService.getStudentsByCaloriesBurned(university));
    }

    @GetMapping("/userLeaderboard")
    public ResponseEntity<List<UserScoreDTO>> getUserLeaderboard() {
        List<UserScoreDTO> userScores = userService.calculateUserScores();
        return ResponseEntity.ok(userScores);
    }

    @GetMapping(path = "/universityRank/{university}")
    public ResponseEntity<?> getUniversityRank(@PathVariable University university) {
        try {
            int rank = activityService.getUniversityRank(university);
            return ResponseEntity.ok(rank);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/userRank/{email}")
    public ResponseEntity<?> getUserRank(@PathVariable String email) {
        try {
            Map<String, Object> ranks = new HashMap<>();
            int scoreRank = userService.getUserRankWithinUniversity(email); // Changed to the new method
            int distanceRank = userService.getUserDistanceRankWithinUniversity(email);
            int caloriesRank = userService.getUserCaloriesRankWithinUniversity(email);
            int speedRank = userService.getUserSpeedRankWithinUniversity(email);

            ranks.put("scoreRank", scoreRank == -1 ? Integer.MAX_VALUE : scoreRank);
            ranks.put("distanceRank", distanceRank == -1 ? Integer.MAX_VALUE : distanceRank);
            ranks.put("caloriesRank", caloriesRank == -1 ? Integer.MAX_VALUE : caloriesRank);
            ranks.put("speedRank", speedRank == -1 ? Integer.MAX_VALUE : speedRank);

            return ResponseEntity.ok(ranks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/rankByDistance/{userEmail}")
    public ResponseEntity<?> getUserDistanceRank(@PathVariable String userEmail) {
        try {
            int rank = userService.getUserDistanceRankWithinUniversity(userEmail);
            return ResponseEntity.ok(Collections.singletonMap("rank", rank));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/globalUserRank/{email}")
    public ResponseEntity<?> getGlobalUserRank(@PathVariable String email) {
        try {
            int globalRank = userService.getUserRank(email);
            return ResponseEntity.ok(Collections.singletonMap("globalRank", globalRank));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}