package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UserDTO;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/studentloppet")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/hello")
    public @ResponseBody String hello() {
        return "helloworld";
    }

    @GetMapping(path = "/addwithuni/{email}/{password}/{university}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email,
            @PathVariable University university) {
        if (userService.emailOccupied(email)) {
            return new IllegalArgumentException("Email already exists").toString();
        }

        // -- SLUT PÅ API CALLS--BORTKOMMENTERAD SÅLÄNGE-- //
        // if (!userService.validateEmail(email)) {
        // return new IllegalArgumentException("Invalid email address").toString();
        // }
        userService.registerUser(email, password, university);
        return "User registered successfully";
    }

    @GetMapping(path = "/add/{email}/{password}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email) {
        if (userService.emailOccupied(email)) {
            return new IllegalArgumentException("Email already exists").toString();
        }
        if (!userService.validateEmail(email)) {
            return new IllegalArgumentException("Invalid email address").toString();
        }
        userService.registerUser(email, password, null);
        return "saved";
    }

    @GetMapping(path = "/getUser/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String email) {
        return userService.getUserInfo(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/login/{email}/{password}")
    public ResponseEntity<UserDTO> login(@PathVariable String email, @PathVariable String password) {
        return userService.login(email, password)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // fixa så att user inte kan skriva otilåtna namn, fixa test
    @GetMapping(path = "/set/{email}/{first}/{last}")
    public @ResponseBody ResponseEntity<?> setName(@PathVariable String email, @PathVariable String first,
            @PathVariable String last) {
        try {
            boolean result = userService.setName(email, first, last);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // fixa så att user inte kan skriva något utöver integers, fixa test
    @GetMapping(path = "/increaseScore/{email}/{value}")
    public @ResponseBody String increaseScore(@PathVariable String email, @PathVariable int value) {
        return userService.increaseScore(email, value);
    }

    @GetMapping(path = "/setWeight/{email}/{weight}")
    public ResponseEntity<Boolean> setWeightCall(@PathVariable String email, @PathVariable double weight) {
        boolean result = userService.setWeight(email, weight);
        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }

    @GetMapping("/userRank/{email}")
    public ResponseEntity<?> getUserRank(@PathVariable String email) {
        try {
            Map<String, Object> ranks = new HashMap<>();
            int scoreRank = userService.getUserRankWithinUniversity(email); // Changed to the new method
            int distanceRank = userService.getUserDistanceRankWithinUniversity(email);
            int caloriesRank = userService.getUserCaloriesRankWithinUniversity(email);
            int speedRank = userService.getUserSpeedRankWithinUniversity(email);

            ranks.put("scoreRank", scoreRank == -1 ? Integer.MAX_VALUE : scoreRank);
            ranks.put("distanceRank", distanceRank == -1 ? Integer.MAX_VALUE : distanceRank);
            ranks.put("caloriesRank", caloriesRank == -1 ? Integer.MAX_VALUE : caloriesRank);
            ranks.put("speedRank", speedRank == -1 ? Integer.MAX_VALUE : speedRank);

            return ResponseEntity.ok(ranks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/globalUserRank/{email}")
    public ResponseEntity<?> getGlobalUserRank(@PathVariable String email) {
        try {
            int globalRank = userService.getUserRank(email);
            return ResponseEntity.ok(Collections.singletonMap("globalRank", globalRank));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}