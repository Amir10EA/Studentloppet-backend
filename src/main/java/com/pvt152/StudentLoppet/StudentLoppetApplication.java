package com.pvt152.StudentLoppet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableScheduling
@CrossOrigin
public class StudentLoppetApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentLoppetApplication.class, args);
	}
}
