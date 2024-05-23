package com.pvt152.StudentLoppet.dto;

import com.pvt152.StudentLoppet.model.University;
public class UserDTO {
    private String email;
    private int score;
    private String firstName;
    private String lastName;
    private University university;
    private String universityDisplayName;
    private int age;
    private String password;
    public UserDTO() {
    }
    public UserDTO(String email, int score, String firstName, String lastName, University university, int age,
                   String password) {
        this.email = email;
        this.score = score;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.universityDisplayName = university != null ? university.getDisplayName() : null;
        this.age = age;
        this.password = password;
    }

    public UserDTO(String email, int score, String firstName, String lastName, University university, int age) {
        this.email = email;
        this.score = score;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.universityDisplayName = university != null ? university.getDisplayName() : null;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}