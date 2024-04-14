package com.pvt152.StudentLoppet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;
    private String lastName;
  
    private University university;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public University getUniversity() {
        return university;
    }

    public User(String firstName, String lastName, University university) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
    }

    

}
