package com.pvt152.StudentLoppet.dto;

import com.pvt152.StudentLoppet.model.University;

public class UserDTO {
    private String email;
    private int score;
    private String firstName;
    private String lastName;
    private University university;
    private String universityDisplayName;

    public UserDTO(String email, int score, String firstName, String lastName, University university) {
        this.email = email;
        this.score = score;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.universityDisplayName = university.getDisplayName();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getUniversityDisplayName() {
        return universityDisplayName;
    }

    public void setUniversityDisplayName(String universityDisplayName) {
        this.universityDisplayName = universityDisplayName;
    }
}
