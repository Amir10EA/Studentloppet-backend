package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.dto.UserDTO;
import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.ActivityService;
import com.pvt152.StudentLoppet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/studentloppet")
@CrossOrigin
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

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
        if (!userService.validateEmail(email)) {
            return new IllegalArgumentException("Invalid email address").toString();
        }
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

    @GetMapping(path = "/set/{email}/{first}/{last}")
    public @ResponseBody boolean setName(@PathVariable String email, @PathVariable String first,
            @PathVariable String last) {
        return userService.setName(email, first, last);
    }

    @GetMapping(path = "/increaseScore/{email}/{value}")
    public @ResponseBody String increaseScore(@PathVariable String email, @PathVariable int value) {
        return userService.increaseScore(email, value);
    }

    @PostMapping(path = "/activity/{email}/{distance}/{duration}")
    public ResponseEntity<?> logActivity(@PathVariable String email,
            @PathVariable double distance,
            @PathVariable long duration) {
        try {
            Activity activity = activityService.logActivity(email, distance, duration);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }

}
