package com.pvt152.StudentLoppet.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pvt152.StudentLoppet.model.ProfilePicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;
import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.model.User;

@Service
public class LeaderboardService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ActivityRepository activityRepository;

        public List<UserScoreDTO> getStudentsByScore(University university) {
                List<User> users = userRepository.findByUniversity(university);
                return users.stream()
                                .map(user -> new UserScoreDTO(user.getFirstName() + " " + user.getLastName(),
                                                user.getEmail(),
                                                user.getScore(), user.getProfilePicture()))
                                .sorted(Comparator.comparingInt(UserScoreDTO::getScore).reversed())
                                .collect(Collectors.toList());
        }

        public List<UserStats> getStudentsByDistance(University university) {
                List<Activity> activities = activityRepository.findByUniversity(university);
                return activities.stream()
                                .collect(Collectors.groupingBy(Activity::getUser,
                                                Collectors.summingDouble(Activity::getDistance)))
                                .entrySet().stream()
                                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(),
                                                e.getValue(),
                                                e.getKey().getProfilePicture()))
                                .sorted(Comparator.comparingDouble(UserStats::getValue).reversed())
                                .collect(Collectors.toList());
        }

        public List<UserStats> getStudentsBySpeed(University university) {
                List<Activity> activities = activityRepository.findByUniversity(university);

                Map<User, Double[]> totals = activities.stream()
                                .collect(Collectors.toMap(
                                                Activity::getUser,
                                                a -> new Double[] { a.getDistance(), (double) a.getDuration() },
                                                (totals1, totals2) -> new Double[] { totals1[0] + totals2[0],
                                                                totals1[1] + totals2[1] }));

                Map<User, Double> userSpeeds = totals.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> {
                                                        Double totalDistance = entry.getValue()[0];
                                                        Double totalDuration = entry.getValue()[1];
                                                        return totalDistance > 0 ? totalDuration / totalDistance : 0;
                                                }));

                return userSpeeds.entrySet().stream()
                                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(),
                                                e.getValue(),
                                                e.getKey().getProfilePicture()))
                                .sorted(Comparator.comparingDouble(UserStats::getValue))
                                .collect(Collectors.toList());
        }

        public List<UserStats> getStudentsByCaloriesBurned(University university) {
                List<Activity> activities = activityRepository.findByUniversity(university);
                return activities.stream()
                                .collect(
                                                Collectors.groupingBy(Activity::getUser,
                                                                Collectors.summingDouble(Activity::getCaloriesBurned)))
                                .entrySet().stream()
                                .map(e -> new UserStats(e.getKey().getFirstName() + " " + e.getKey().getLastName(),
                                                e.getValue(),
                                                e.getKey().getProfilePicture()))
                                .sorted(Comparator.comparingDouble(UserStats::getValue).reversed())
                                .collect(Collectors.toList());
        }

        public List<UserScoreDTO> calculateUserScores() {
                return userRepository.findScoresByUser().stream()
                                .map(result -> new UserScoreDTO(
                                                (String) result[1],
                                                (String) result[0],
                                                ((Number) result[2]).intValue(),
                                                (ProfilePicture) result[3]))
                                .collect(Collectors.toList());
        }

}