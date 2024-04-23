package com.pvt152.StudentLoppet.controller;

import com.pvt152.StudentLoppet.model.University;
import com.pvt152.StudentLoppet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/studentloppet")
@CrossOrigin
public class MainController {

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
        userService.registerUser(email, password, university);
        return "saved";
    }

    @GetMapping(path = "/add/{email}/{password}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email) {
        if (userService.emailOccupied(email)) {
            return new IllegalArgumentException("Email already exists").toString();
        }
        userService.registerUser(email, password, null);
        return "saved";
    }

    @GetMapping(path = "/login/{email}/{password}")
    public @ResponseBody boolean login(@PathVariable String email, @PathVariable String password) {
        return userService.login(email, password);
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

    @GetMapping(path = "/test")
    public @ResponseBody ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working");
    }
}
