package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UniversityMetricDTO;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.UniversityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class UniversityControllerTest {

    @Mock
    private UniversityService universityService;

    @InjectMocks
    private UniversityController universityController;

    private static final University UNIVERSITY = University.STOCKHOLMS_UNIVERSITET;



    @Test
    void getUniversityLeaderboard() {
        List<UniversityMetricDTO> expectedScores = Arrays.asList(new UniversityMetricDTO("Stockholm", 100));
        when(universityService.calculateUniversityScores()).thenReturn(expectedScores);

        ResponseEntity<List<UniversityMetricDTO>> response = universityController.getUniversityLeaderboard();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedScores);
    }

    @Test
    void getUniversitiesByUserCount() {
        List<UniversityMetricDTO> expectedUserCounts = Arrays.asList(new UniversityMetricDTO("Stockholm", 200));
        when(universityService.countUsersByUniversity()).thenReturn(expectedUserCounts);

        ResponseEntity<List<UniversityMetricDTO>> response = universityController.getUniversitiesByUserCount();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedUserCounts);
    }

    @Test
    void getUniversitiesByDistance() {
        List<UniversityMetricDTO> expectedDistances = Arrays.asList(new UniversityMetricDTO("Stockholm", 300));
        when(universityService.sumDistanceByUniversity()).thenReturn(expectedDistances);

        ResponseEntity<List<UniversityMetricDTO>> response = universityController.getUniversitiesByDistance();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedDistances);
    }

    @Test
    void getUniversityRank_success() {
        int expectedRank = 1;
        when(universityService.getUniversityRank(UNIVERSITY)).thenReturn(expectedRank);

        ResponseEntity<?> response = universityController.getUniversityRank(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedRank);
    }

    @Test
    void getUniversityRank_failure() {
        String errorMessage = "University not found";
        when(universityService.getUniversityRank(UNIVERSITY)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> response = universityController.getUniversityRank(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Error: " + errorMessage);
    }
}
