package com.pvt152.StudentLoppet.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.dto.UniversityScoreDTO;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;

@Service
public class UniversityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UniversityScoreDTO> calculateUniversityScores() {
        return userRepository.findScoresByUniversity().stream()
                .map(result -> new UniversityScoreDTO(
                        ((University) result[0]).getDisplayName(),
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
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

    public Map<String, Integer> countUsersByUniversity() {
        return userRepository.countUsersByUniversity().stream()
                .collect(Collectors.toMap(
                        entry -> ((University) entry[0]).getDisplayName(),
                        entry -> ((Number) entry[1]).intValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public Map<String, Double> sumDistanceByUniversity() {
        return activityRepository.sumDistanceByUniversity().stream()
                .collect(Collectors.toMap(
                        entry -> ((University) entry[0]).getDisplayName(),
                        entry -> ((Number) entry[1]).doubleValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

}
