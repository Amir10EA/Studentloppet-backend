package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.LeaderboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/sortedByScore/{university}")
    public ResponseEntity<List<UserScoreDTO>> sortedByScore(@PathVariable University university) {
        try {
            List<UserScoreDTO> userScores = leaderboardService.getStudentsByScore(university);
            return ResponseEntity.ok(userScores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/sortedByDistance/{university}")
    public ResponseEntity<List<UserStats>> sortedByDistance(@PathVariable University university) {
        return ResponseEntity.ok(leaderboardService.getStudentsByDistance(university));
    }

    @GetMapping("/sortedBySpeed/{university}")
    public ResponseEntity<List<UserStats>> sortedBySpeed(@PathVariable University university) {
        return ResponseEntity.ok(leaderboardService.getStudentsBySpeed(university));
    }

    @GetMapping("/sortedByCalories/{university}")
    public ResponseEntity<List<UserStats>> sortedByCalories(@PathVariable University university) {
        return ResponseEntity.ok(leaderboardService.getStudentsByCaloriesBurned(university));
    }

    @GetMapping("/userLeaderboard")
    public ResponseEntity<List<UserScoreDTO>> getUserLeaderboard() {
        List<UserScoreDTO> userScores = leaderboardService.calculateUserScores();
        return ResponseEntity.ok(userScores);
    }

}
