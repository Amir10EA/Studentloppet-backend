package com.pvt152.StudentLoppet.service;

import com.pvt152.StudentLoppet.dto.UserDTO;
import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.model.User;
import com.pvt152.StudentLoppet.repository.ActivityRepository;
import com.pvt152.StudentLoppet.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public User registerUser(String email, String password, University university) {
        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordHashing(password));
        if (university != null) {
            u.setUniversity(university);
        }
        userRepository.save(u);
        return u;
    }

    public void setYearOfBirth(String email, int yearOfBirth) {
        User user = userRepository.findById(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setYearOfBirth(yearOfBirth);
        userRepository.save(user);
    }

    public boolean setName(String email, String first, String last) {

        if (!isValidName(first) || !isValidName(last)) {
            throw new IllegalArgumentException("Names must contain only alphabetic characters and spaces.");
        }
        User u = userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        u.setFirstName(first);
        u.setLastName(last);

        userRepository.save(u);
        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$");
    }

    public Optional<UserDTO> login(String email, String password) {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent() && passwordHashing(password).equals(user.get().getPassword())) {
            return getUserInfo(email);
        } else {
            return Optional.empty();
        }
    }

    public String increaseScore(String email, int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Score must be a positive number");

        }

        User u = userRepository.findById(email).orElseThrow(() -> new IllegalStateException("User not found"));
        u.setScore(u.getScore() + value);
        userRepository.save(u);
        return "Increased " + email + " score by " + value;
    }

    public String passwordHashing(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<UserDTO> getUserInfo(String email) {
        return userRepository.findById(email).map(user -> new UserDTO(
                user.getEmail(),
                user.getScore(),
                user.getFirstName(),
                user.getLastName(),
                user.getUniversity(),
                user.getYearOfBirth()));
    }

    public boolean emailOccupied(String email) {
        return userRepository.existsById(email);
    }

    public boolean validateEmail(String email) {
        WebClient client = WebClient.create("https://emailvalidation.abstractapi.com");
        try {
            String result = client.get()
                    .uri(uriBuilder -> uriBuilder.path("/v1/")
                            .queryParam("api_key", "bf7e1f869c184338aca90578ac66b448")
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(result);
            return "DELIVERABLE".equals(jsonNode.get("deliverability").asText());
        } catch (Exception e) {
            return false;
        }
    }

    public int getUserRank(String userEmail) {
        List<Object[]> scores = userRepository.findScoresByUser();
        Integer lastScore = null;
        int rank = 1;

        for (Object[] score : scores) {
            String email = (String) score[0];
            int userScore = ((Number) score[2]).intValue();

            if (lastScore != null && userScore != lastScore) {
                rank++;
            }

            if (email.equals(userEmail)) {
                return rank;
            }

            lastScore = userScore;
        }

        return -1;
    }

    public int getUserDistanceRankWithinUniversity(String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getUniversity() == null) {
            throw new RuntimeException("User does not belong to any university");
        }

        List<Object[]> distances = activityRepository.findTotalDistanceByUniversity(user.getUniversity());
        if (distances.isEmpty()) {
            return 1;
        }

        distances.sort((a, b) -> Double.compare((Double) b[1], (Double) a[1]));

        Integer lastDistance = null;
        int rank = 1;
        int lastAssignedRank = 1;

        for (Object[] distance : distances) {
            String email = (String) distance[0];
            int currentDistance = ((Number) distance[1]).intValue();

            if (lastDistance != null && currentDistance != lastDistance) {
                rank++;
            }

            lastAssignedRank = rank;

            if (email.equals(userEmail)) {
                return rank;
            }

            lastDistance = currentDistance;
        }

        return lastAssignedRank + 1;
    }

    public int getUserCaloriesRankWithinUniversity(String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        List<Object[]> calories = activityRepository.findTotalCaloriesBurnedByUniversity(user.getUniversity());
        return calculateRank(userEmail, calories);
    }

    public int getUserSpeedRankWithinUniversity(String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        List<Object[]> results = activityRepository.findTotalDistanceAndDurationByUniversity(user.getUniversity());
        List<Double> speeds = new ArrayList<>();
        Map<String, Double> emailToSpeedMap = new HashMap<>();

        for (Object[] result : results) {
            String email = (String) result[0];
            Double totalDistance = ((Number) result[1]).doubleValue();
            Double totalDuration = ((Number) result[2]).doubleValue();
            double speed = totalDuration > 0 ? totalDistance / (totalDuration / 60.0) : 0; // Speed in km/min
            speeds.add(speed);
            emailToSpeedMap.put(email, speed);
        }

        Collections.sort(speeds, Collections.reverseOrder());

        double userSpeed = emailToSpeedMap.getOrDefault(userEmail, -1.0);
        if (userSpeed == -1) {
            int lastRank = speeds.size() + 1;
            return lastRank;
        }

        int rank = 1;
        double lastSpeed = -1;
        for (double speed : speeds) {
            if (speed != lastSpeed) {
                if (speed == userSpeed) {
                    return rank;
                }
                rank++;
                lastSpeed = speed;
            } else if (speed == userSpeed) {
                return rank;
            }
        }
        return -1;
    }

    private int calculateRank(String userEmail, List<Object[]> results) {
        if (results.isEmpty()) {
            return 1;
        }

        results.sort((a, b) -> Double.compare(((Number) b[1]).doubleValue(), ((Number) a[1]).doubleValue()));

        int rank = 1;
        Integer lastValue = null;
        int lastRank = 1;
        boolean foundUser = false;
        int lastPositiveValueRank = 0;
        int lastSeenValue = 0;

        for (int i = 0; i < results.size(); i++) {
            String email = (String) results.get(i)[0];
            int currentValue = ((Number) results.get(i)[1]).intValue();
            lastSeenValue = currentValue;

            if (lastValue != null && currentValue < lastValue) {
                if (currentValue > 0) {
                    rank = i + 1;
                } else {
                    rank = lastPositiveValueRank + 1;
                }
            }

            if (currentValue > 0) {
                lastPositiveValueRank = rank;
            }

            if (email.equals(userEmail)) {
                foundUser = true;
                return rank;
            }

            lastValue = currentValue;
            lastRank = rank;
        }


        if (!foundUser) {
            return lastSeenValue > 0 ? lastPositiveValueRank + 1 : lastPositiveValueRank; // Assign rank one below the

        }

        return -1;
    }

    public int getUserRankWithinUniversity(String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getUniversity() == null) {
            throw new RuntimeException("User does not belong to any university");
        }

        List<Object[]> scores = userRepository.findScoresByUniversity(user.getUniversity());
        Integer lastScore = null;
        int rank = 1;

        for (Object[] score : scores) {
            String email = (String) score[0];
            int userScore = ((Number) score[1]).intValue();

            if (lastScore != null && userScore != lastScore) {
                rank++;
            }

            if (email.equals(userEmail)) {
                return rank;
            }

            lastScore = userScore;
        }

        return -1;
    }

    public boolean setWeight(String email, double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be a positive number");
        }

        User user = userRepository.findById(email).orElse(null);
        if (user == null) {
            return false;
        }

        user.setWeight(weight);
        userRepository.save(user);
        return true;
    }

}