package com.pvt152.StudentLoppet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/pet")
public class MainController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(value = "/hello")
    public @ResponseBody String hello() {
        return "Nyare än nytt, Yeahoooo";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping(path = "/add/{studentName}/{University}")
    public @ResponseBody String addNewStudent(@PathVariable String studentName, @PathVariable String university) {

        Student student = new Student();
        student.setName(studentName);
        student.setUniversity(university);
        studentRepository.save(student);
        return "saved"; // return safe only if there was no errors, lämnar den såhär sålänge.
    }
}
