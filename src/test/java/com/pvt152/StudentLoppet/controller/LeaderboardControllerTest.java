package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.ProfilePicture;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.LeaderboardService;
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
class LeaderboardControllerTest {

    @Mock
    private LeaderboardService leaderboardService;

    @InjectMocks
    private LeaderboardController leaderboardController;

    private static final University UNIVERSITY = University.STOCKHOLMS_UNIVERSITET;

    private ProfilePicture createMockProfilePicture() {
        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setId(1L);
        profilePicture.setFilename("a.jpg");
        profilePicture.setMimeType("image/jpeg");
        return profilePicture;
    }

    @Test
    void sortedByScore_success() {
        ProfilePicture profilePicture = createMockProfilePicture();
        List<UserScoreDTO> expectedScores = Arrays.asList(
                new UserScoreDTO("Student1", "student1@example.com", 100, profilePicture)
        );
        when(leaderboardService.getStudentsByScore(UNIVERSITY)).thenReturn(expectedScores);

        ResponseEntity<List<UserScoreDTO>> response = leaderboardController.sortedByScore(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedScores);
    }

    @Test
    void sortedByScore_failure() {
        when(leaderboardService.getStudentsByScore(UNIVERSITY)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<UserScoreDTO>> response = leaderboardController.sortedByScore(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void sortedByDistance_success() {
        ProfilePicture profilePicture = createMockProfilePicture();
        List<UserStats> expectedStats = Arrays.asList(
                new UserStats("Student1", 100.0, profilePicture)
        );
        when(leaderboardService.getStudentsByDistance(UNIVERSITY)).thenReturn(expectedStats);

        ResponseEntity<List<UserStats>> response = leaderboardController.sortedByDistance(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedStats);
    }

    @Test
    void sortedBySpeed_success() {
        ProfilePicture profilePicture = createMockProfilePicture();
        List<UserStats> expectedStats = Arrays.asList(
                new UserStats("Student1", 10.0, profilePicture)
        );
        when(leaderboardService.getStudentsBySpeed(UNIVERSITY)).thenReturn(expectedStats);

        ResponseEntity<List<UserStats>> response = leaderboardController.sortedBySpeed(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedStats);
    }

    @Test
    void sortedByCalories_success() {
        ProfilePicture profilePicture = createMockProfilePicture();
        List<UserStats> expectedStats = Arrays.asList(
                new UserStats("Student1", 500, profilePicture)
        );
        when(leaderboardService.getStudentsByCaloriesBurned(UNIVERSITY)).thenReturn(expectedStats);

        ResponseEntity<List<UserStats>> response = leaderboardController.sortedByCalories(UNIVERSITY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedStats);
    }

    @Test
    void getUserLeaderboard() {
        ProfilePicture profilePicture = createMockProfilePicture();
        List<UserScoreDTO> expectedScores = Arrays.asList(
                new UserScoreDTO("Student1", "student1@example.com", 100, profilePicture)
        );
        when(leaderboardService.calculateUserScores()).thenReturn(expectedScores);

        ResponseEntity<List<UserScoreDTO>> response = leaderboardController.getUserLeaderboard();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedScores);
    }
}
