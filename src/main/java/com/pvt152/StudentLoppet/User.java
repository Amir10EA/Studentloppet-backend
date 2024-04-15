package com.pvt152.StudentLoppet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private String email;

    private String firstName;
    private String lastName;
    private String password;

    private String university;

    
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUniversity() {
        return university;
    }

    public User(String firstName, String lastName, String password, String email, String university) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    

}
