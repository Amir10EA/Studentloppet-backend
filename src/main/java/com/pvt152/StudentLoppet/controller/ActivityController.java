package com.pvt152.StudentLoppet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.ActivityService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping(path = "/addActivity/{email}/{distance}/{duration}")
    public ResponseEntity<?> logActivity(@PathVariable String email,
            @PathVariable("distance") double distance,
            @PathVariable("duration") long duration) {
        try {
            if (Double.isNaN(distance) || distance < 0 || duration < 0) {
                throw new IllegalArgumentException("Invalid distance or duration");
            }

            double durationInMinutes = duration / 60.0;
            Activity activity = activityService.logActivity(email, distance, durationInMinutes);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/total/{email}")
    public ResponseEntity<?> getTotalDistanceAndDuration(@PathVariable String email) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDuration(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/totalWeekSummary/{email}")
    public ResponseEntity<?> getTotalDistanceAndDurationForWeek(@PathVariable String email) {
        try {
            Map<String, Object> result = activityService.getTotalDistanceAndDurationPastWeek(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

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