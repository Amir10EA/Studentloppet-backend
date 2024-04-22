package com.pvt152.StudentLoppet;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UniversityLeaderboardService {
    private final UserRepository userRepository;

    public UniversityLeaderboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UniversityScoreDTO> calculateUniversityScores() {
        return userRepository.findScoresByUniversity().stream()
                .map(result -> new UniversityScoreDTO(
                        (University) result[0], // Use University directly
                        ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }
}
