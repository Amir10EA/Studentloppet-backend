package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UniversityScoreDTO;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.UniversityLeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/universities")
public class LeaderboardController {

    private final UniversityLeaderboardService leaderboardService;

    public LeaderboardController(UniversityLeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping(path = "/scoreboard")
    public @ResponseBody ResponseEntity<List<UniversityScoreDTO>> getUniversityLeaderboard() {
        List<UniversityScoreDTO> scores = leaderboardService.calculateUniversityScores();
        return ResponseEntity.ok(scores);
    }

    @GetMapping(path = "/representation")
    public @ResponseBody Map<String, String> getUniversityRepresentations() {
        Map<String, String> universityMap = new LinkedHashMap<>();
        for (University university : University.values()) {
            universityMap.put(university.name(), university.getDisplayName());
        }
        return universityMap;
    }

    // test
    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }
}
