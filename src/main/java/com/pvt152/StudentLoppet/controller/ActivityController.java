package com.pvt152.StudentLoppet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.service.ActivityService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // Denna endpoint används för att posta nya aktiviteter till databasen.
    @PostMapping(path = "/activity/{email}/{distance}/{duration}")
    public ResponseEntity<?> logActivity(@PathVariable String email,
            @PathVariable double distance,
            @PathVariable long duration) {
        try {
            Activity activity = activityService.logActivity(email, distance, duration);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Denna endpoint används för få fram en sammanfattning över alla aktiviterer
    // från en användare (sammanlagd distance, duration, calories burned, avarage
    // speed (min/km))
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
    // calories burned, avarage speed (min/km))
    @GetMapping(path = "/TotalWeekSummary/{email}")
    public ResponseEntity<?> getTotalDistanceAndDurationForWeek(@PathVariable String email) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDurationPastWeek(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}