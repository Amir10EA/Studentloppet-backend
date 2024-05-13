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
        return name.matches("^[A-Za-z ]+$");
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
        if(value <0){
            throw new IllegalArgumentException ("Score must be a positive number");

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
                user.getUniversity()));
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
        Integer lastScore = null; // This will store the last user's score for comparison
        int rank = 1; // This will maintain the current rank

        for (Object[] score : scores) {
            String email = (String) score[0];
            int userScore = ((Number) score[2]).intValue();

            // If score changes and it's not the first entry, increment rank
            if (lastScore != null && userScore != lastScore) {
                rank++;
            }

            // Check if the current email matches the requested user's email
            if (email.equals(userEmail)) {
                return rank; // Return the current rank if the email matches
            }

            lastScore = userScore; // Update the lastScore to the current user's score
        }

        return -1; // Return -1 if the user is not found
    }

    public int getUserDistanceRankWithinUniversity(String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getUniversity() == null) {
            throw new RuntimeException("User does not belong to any university");
        }

        // Fetch distances for all users in the same university
        List<Object[]> distances = activityRepository.findTotalDistanceByUniversity(user.getUniversity());
        if (distances.isEmpty()) {
            return 1; // If no distances are found, assume the university has no activities and assign
            // rank 1.
        }

        // Sort the list based on distance in descending order
        distances.sort((a, b) -> Double.compare((Double) b[1], (Double) a[1]));

        Integer lastDistance = null; // This will store the last user's distance for comparison
        int rank = 1; // This will maintain the current rank
        int lastAssignedRank = 1; // To keep track of the last assigned rank

        for (Object[] distance : distances) {
            String email = (String) distance[0];
            int currentDistance = ((Number) distance[1]).intValue();

            // If distance changes and it's not the first entry, increment rank
            if (lastDistance != null && currentDistance != lastDistance) {
                rank++;
            }

            // Update the rank as the last assigned rank
            lastAssignedRank = rank;

            // Check if the current email matches the requested user's email
            if (email.equals(userEmail)) {
                return rank; // Return the current rank if the email matches
            }

            lastDistance = currentDistance; // Update the lastDistance to the current user's distance
        }

        // If the user wasn't found in the list, it means they have no activities.
        // They should get a rank one below the last ranked user.
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

        // Sort speeds to rank them
        Collections.sort(speeds, Collections.reverseOrder());

        // Find rank of the specific user
        double userSpeed = emailToSpeedMap.getOrDefault(userEmail, -1.0);
        if (userSpeed == -1) {
            // If the user does not have any activities, assign them the last rank
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
        return -1; // In case there is no matching speed, though this should not occur
    }

    private int calculateRank(String userEmail, List<Object[]> results) {
        if (results.isEmpty()) {
            return 1; // Default rank if no results.
        }

        results.sort((a, b) -> Double.compare(((Number) b[1]).doubleValue(), ((Number) a[1]).doubleValue()));

        int rank = 1;
        Integer lastValue = null;
        int lastRank = 1;
        boolean foundUser = false;
        int lastPositiveValueRank = 0; // Tracks the last rank for positive values.
        int lastSeenValue = 0; // Tracks the last seen value to handle users with no activities correctly at
        // the end.

        for (int i = 0; i < results.size(); i++) {
            String email = (String) results.get(i)[0];
            int currentValue = ((Number) results.get(i)[1]).intValue();
            lastSeenValue = currentValue; // Update lastSeenValue with the current value

            if (lastValue != null && currentValue < lastValue) {
                if (currentValue > 0) {
                    rank = i + 1; // Update rank if value is less and positive
                } else {
                    rank = lastPositiveValueRank + 1; // Assign next rank to zero results after the last positive value
                }
            }

            if (currentValue > 0) {
                lastPositiveValueRank = rank; // Update last positive value rank
            }

            if (email.equals(userEmail)) {
                foundUser = true;
                return rank; // Return the rank of the matching user
            }

            lastValue = currentValue;
            lastRank = rank;
        }

        // If the user was not found in the list and they are assumed to have no
        // activities
        if (!foundUser) {
            return lastSeenValue > 0 ? lastPositiveValueRank + 1 : lastPositiveValueRank; // Assign rank one below the
            // last positive, or share
            // rank if last is zero
        }

        return -1; // If user is not found in the list
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

        return -1; // Return -1 if the user is not found
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