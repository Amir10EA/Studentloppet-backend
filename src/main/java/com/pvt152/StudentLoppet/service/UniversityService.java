package com.pvt152.StudentLoppet.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.dto.UniversityMetricDTO;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;

@Service
public class UniversityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UniversityMetricDTO> calculateUniversityScores() {
        return userRepository.findScoresByUniversity().stream()
                .map(result -> new UniversityMetricDTO(
                        ((University) result[0]).getDisplayName(),
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public int getUniversityRank(University university) {
        List<UniversityMetricDTO> universityScores = calculateUniversityScores();
        int rank = 1;
        int actualRank = 1;
        Integer lastScore = null;

        for (UniversityMetricDTO universityScore : universityScores) {
            Integer currentScore = (Integer) universityScore.getMetric();

            if (lastScore == null || !lastScore.equals(currentScore)) {
                lastScore = currentScore;
                rank = actualRank;
            }
            if (universityScore.getUniversityDisplayName().equals(university.getDisplayName())) {
                return rank;
            }
            actualRank++;
        }
        return -1;
    }

    public List<UniversityMetricDTO> countUsersByUniversity() {
        return userRepository.countUsersByUniversity().stream()
                .map(result -> new UniversityMetricDTO(
                        ((University) result[0]).getDisplayName(),
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public List<UniversityMetricDTO> sumDistanceByUniversity() {
        return activityRepository.sumDistanceByUniversity().stream()
                .map(result -> new UniversityMetricDTO(
                        ((University) result[0]).getDisplayName(),
                        ((Number) result[1]).doubleValue()))
                .sorted(Comparator.comparingDouble(UniversityMetricDTO::getMetricAsDouble).reversed())
                .collect(Collectors.toList());
    }
}