package com.pvt152.StudentLoppet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/studentloppet")
@CrossOrigin
public class MainController {

    private static final String DELIVERABLE = "DELIVERABLE";

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/hello")
    public @ResponseBody String hello() {
        return "helloworld";
    }

    @GetMapping(path = "/addwithuni/{email}/{password}/{university}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email,
            @PathVariable University university) {

        if (emailOccupied(email)) {
            return new IllegalArgumentException("Email already exists").toString();
        }
        if (!validateEmail(email)) {
            return new IllegalArgumentException("Email not valid").toString();
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordHashing(password));
        u.setUniversity(university);
        userRepository.save(u);
        return "saved";
    }

    @GetMapping(path = "/add/{email}/{password}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email) {

        if (emailOccupied(email)) {
            return new IllegalArgumentException("Email already exists").toString();
        }
        if (!validateEmail(email)) {
            return new IllegalArgumentException("Email not valid").toString();
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordHashing(password));
        userRepository.save(u);

        return "saved";

    }

    @GetMapping(path = "/set/{email}/{first}/{last}")
    public @ResponseBody boolean setName(@PathVariable String email, @PathVariable String first,
            @PathVariable String last) {

        try {

            User u = userRepository.findById(email).orElseThrow(IllegalArgumentException::new);

            u.setFirstName(first);
            u.setLastName(last);
            userRepository.save(u);
            return true;

        } catch (IllegalArgumentException userNotFoundException) {
            return false;
        }
    }

    private String passwordHashing(String password) {
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

    @GetMapping(path = "/login/{email}/{password}")
    public @ResponseBody boolean login(@PathVariable String email, @PathVariable String password) {
        try {

            User u = userRepository.findById(email).orElseThrow(IllegalArgumentException::new);
            String hasedPassword = passwordHashing(password);
            if (hasedPassword.equals(u.getPassword())) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException userNotFoundException) {
            throw new IllegalStateException("user not found");
        }

    }

    @GetMapping(path = "/increaseScore/{email}/{value}")
    public @ResponseBody String increaseScore(@PathVariable String email, @PathVariable int value) {
        try {
            User u = userRepository.findById(email).orElseThrow(() -> new IllegalStateException("User not found"));
            u.setScore(u.getScore() + value);
            userRepository.save(u);
            return "Increased " + email + " score by " + value;
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    private boolean emailOccupied(String email) {
        return userRepository.existsById(email);
    }

    private boolean validateEmail(String email) {
        WebClient client = WebClient.create("https://emailvalidation.abstractapi.com");
        try {
            String result = client.get()
                    .uri("/v1/?api_key=bf7e1f869c184338aca90578ac66b448&email=" + email)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(result);
            String delivarable = jsonNode.get("deliverability").asText();
            return delivarable.equals(DELIVERABLE);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }

    @RestController
    @RequestMapping(path = "/api/universities")
    public class LeaderboardController {
        private final UniversityLeaderboardService leaderboardService;

        public LeaderboardController(UniversityLeaderboardService leaderboardService) {
            this.leaderboardService = leaderboardService;
        }

        @GetMapping(path = "/scoreboard")
        public @ResponseBody ResponseEntity<List<UniversityScoreDTO>> getUniversityLeaderboard() {
            List<UniversityScoreDTO> scores = leaderboardService.calculateUniversityScores();
            return ResponseEntity.ok(scores);
        }

        @GetMapping(path = "/representation")
        public @ResponseBody Map<String, String> getUniversityRepresentations() {
            Map<String, String> universityMap = new LinkedHashMap<>();
            for (University university : University.values()) {
                universityMap.put(university.name(), university.getDisplayName());
            }
            return universityMap;
        }
    }
}