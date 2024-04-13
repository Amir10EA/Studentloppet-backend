package com.pvt152.StudentLoppet;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path = "/student")
@CrossOrigin
public class MainController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(value = "/hello")
    public @ResponseBody String hello() {
        return "Hello from the pet server";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Student> getStudents() {

        for (Student checkStudent : studentRepository.findAll()) {

            int strikes = 0;
            if (checkStudent.getLastActiveDate() != null && (checkStudent.getLastActiveDate()
                    .plusHours(checkStudent.getIntervalRegistrationDate()).isBefore(LocalDateTime.now()))) {
                strikes++;
            }

            if (checkStudent.getRegistrationDate() != null && (checkStudent.getRegistrationDate()
                    .plusHours(checkStudent.getIntervalRegistrationDate()).isBefore(LocalDateTime.now()))) {
                strikes++;
            }

            if (strikes > 2) {
                studentRepository.delete(checkStudent);
            }
        }

        return studentRepository.findAll();
    }

    @GetMapping(path = "/add/{studentName}/{university}")
    public @ResponseBody String addNewPet(@PathVariable String studentName, @PathVariable String university) {

        Student student = new Student();
        student.setName(studentName);
        student.setUniversity(university);
        studentRepository.save(student);
        return "Saved";

    }

}
