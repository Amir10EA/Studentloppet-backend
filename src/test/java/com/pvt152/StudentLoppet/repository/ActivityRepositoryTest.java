package com.pvt152.StudentLoppet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;

@ExtendWith(MockitoExtension.class)
public class ActivityRepositoryTest {

    @Mock
    private ActivityRepository activityRepository;

    private static final University UNIVERSITY = University.STOCKHOLMS_UNIVERSITET;

    @Test
    public void testFindByUserEmail() {
        String email = "test@example.com";
        List<Activity> activities = List.of(new Activity(), new Activity());

        Mockito.when(activityRepository.findByUserEmail(email)).thenReturn(activities);

        List<Activity> result = activityRepository.findByUserEmail(email);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindByUserEmailAndTimestampAfter() {
        String email = "test@example.com";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        List<Activity> activities = List.of(new Activity(), new Activity());

        Mockito.when(activityRepository.findByUserEmailAndTimestampAfter(email, startDate)).thenReturn(activities);

        List<Activity> result = activityRepository.findByUserEmailAndTimestampAfter(email, startDate);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindByUniversity() {
        List<Activity> activities = List.of(new Activity(), new Activity());

        Mockito.when(activityRepository.findByUniversity(UNIVERSITY)).thenReturn(activities);

        List<Activity> result = activityRepository.findByUniversity(UNIVERSITY);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testSumDistanceByUniversity() {
        Object[] result1 = {University.STOCKHOLMS_UNIVERSITET, 100};
        Object[] result2 = {University.UPPSALA_UNIVERSITET, 200};
        List<Object[]> distances = List.of(result1, result2);

        Mockito.when(activityRepository.sumDistanceByUniversity()).thenReturn(distances);

        List<Object[]> result = activityRepository.sumDistanceByUniversity();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindTotalDistanceByUniversity() {
        Object[] result1 = {"user1@example.com", 100};
        Object[] result2 = {"user2@example.com", 200};
        List<Object[]> distances = List.of(result1, result2);

        Mockito.when(activityRepository.findTotalDistanceByUniversity(UNIVERSITY)).thenReturn(distances);

        List<Object[]> result = activityRepository.findTotalDistanceByUniversity(UNIVERSITY);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindTotalCaloriesBurnedByUniversity() {
        Object[] result1 = {"user1@example.com", 100};
        Object[] result2 = {"user2@example.com", 200};
        List<Object[]> calories = List.of(result1, result2);

        Mockito.when(activityRepository.findTotalCaloriesBurnedByUniversity(UNIVERSITY)).thenReturn(calories);

        List<Object[]> result = activityRepository.findTotalCaloriesBurnedByUniversity(UNIVERSITY);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindAverageSpeedByUniversity() {
        Object[] result1 = {"user1@example.com", 10.0};
        Object[] result2 = {"user2@example.com", 20.0};
        List<Object[]> speeds = List.of(result1, result2);

        Mockito.when(activityRepository.findAverageSpeedByUniversity(UNIVERSITY)).thenReturn(speeds);

        List<Object[]> result = activityRepository.findAverageSpeedByUniversity(UNIVERSITY);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindTotalDistanceAndDurationByUniversity() {
        Object[] result1 = {"user1@example.com", 100, 60};
        Object[] result2 = {"user2@example.com", 200, 120};
        List<Object[]> data = List.of(result1, result2);

        Mockito.when(activityRepository.findTotalDistanceAndDurationByUniversity(UNIVERSITY)).thenReturn(data);

        List<Object[]> result = activityRepository.findTotalDistanceAndDurationByUniversity(UNIVERSITY);
        assertThat(result).hasSize(2);
    }
}