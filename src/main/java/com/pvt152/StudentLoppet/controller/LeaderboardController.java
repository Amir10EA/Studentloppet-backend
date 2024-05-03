package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UniversityScoreDTO;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UniversityLeaderboardService;
import com.pvt152.StudentLoppet.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/universities")
public class LeaderboardController {

    private final UniversityLeaderboardService leaderboardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    public LeaderboardController(UniversityLeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping(path = "/representation")
    public @ResponseBody Map<String, String> getUniversityRepresentations() {
        Comparator<String> caseInsensitiveOrder = String.CASE_INSENSITIVE_ORDER;

        Map<String, String> universityMap = Stream.of(University.values())
                .collect(Collectors.toMap(
                        University::name,
                        University::getDisplayName,
                        (oldValue, newValue) -> oldValue,
                        () -> new TreeMap<>(caseInsensitiveOrder)));

        return universityMap;
    }

    @GetMapping(path = "/scoreboard")
    public @ResponseBody ResponseEntity<List<UniversityScoreDTO>> getUniversityLeaderboard() {
        List<UniversityScoreDTO> scores = leaderboardService.calculateUniversityScores();
        return ResponseEntity.ok(scores);
    }

    // test
    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }

    @GetMapping("/universitiesByUserCount")
    public ResponseEntity<Map<String, Integer>> getUniversitiesByUserCount() {
        Map<String, Integer> universityUserCounts = userService.countUsersByUniversity();
        return ResponseEntity.ok(universityUserCounts);
    }

    @GetMapping("/universitiesByDistance")
    public ResponseEntity<Map<String, Double>> getUniversitiesByDistance() {
        Map<String, Double> universityDistances = activityService.sumDistanceByUniversity();
        return ResponseEntity.ok(universityDistances);
    }
}
