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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        User u = userRepository.findById(email).orElseThrow(IllegalArgumentException::new);
        u.setFirstName(first);
        u.setLastName(last);
        userRepository.save(u);
        return true;
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

    public Map<String, Integer> countUsersByUniversity() {
        return userRepository.countUsersByUniversity().stream()
                .collect(Collectors.toMap(
                        entry -> ((University) entry[0]).getDisplayName(),
                        entry -> ((Number) entry[1]).intValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public List<UserScoreDTO> calculateUserScores() {
        return userRepository.findScoresByUser().stream()
                .map(result -> new UserScoreDTO(
                        (String) result[1], // userName as concatenated fullName
                        (String) result[0], // email
                        ((Number) result[2]).intValue())) // score
                .collect(Collectors.toList());
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

    public boolean setWeightAndHeight(String email, double weight, double height) {
        User user = userRepository.findById(email).orElse(null);
        if (user == null) {
            return false;
        }
        user.setWeight(weight);
        user.setHeight(height);
        userRepository.save(user);
        return true;
    }
}