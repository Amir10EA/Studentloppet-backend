package com.pvt152.StudentLoppet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping(path = "/studentloppet")
@CrossOrigin
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/hello")
    public @ResponseBody String hello() {
        return "helloworld";
    }

    @GetMapping(path = "/add/{email}/{password}")
    public @ResponseBody String register(@PathVariable String password, @PathVariable String email) {

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordHashing(password));
        userRepository.save(u);

        return "Saved";

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

    @GetMapping(path = "/set/{email}/{university}")
    public @ResponseBody boolean setUniversity(@PathVariable String email, @PathVariable String university) {

        try {

            User u = userRepository.findById(email).orElseThrow(IllegalArgumentException::new);

            u.setUniversity(university);
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

}