package com.pvt152.StudentLoppet.model;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "`user`")
public class User {

    @Id
    private String email;
    private int score;
    private String firstName;
    private String lastName;
    private String password;
    private double weight;
    private int yearOfBirth;

    @Enumerated(EnumType.STRING)
    private University university;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
    private ProfilePicture profilePicture;

    @OneToOne
    private ForgotPassword forgotPassword;

    public User() {
    }

    @Builder
    public User(String email, int score, String firstName, String lastName, String password, double weight,
            University university, ProfilePicture profilePicture, int yearOfBirth) {
        this.email = email;
        this.score = score;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.weight = weight;
        this.university = university;
        this.profilePicture = profilePicture;
        this.yearOfBirth = yearOfBirth;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}