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

}