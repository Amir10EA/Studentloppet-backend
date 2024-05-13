package com.pvt152.StudentLoppet.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Enumerated(EnumType.STRING)
    private University university;

    @OneToOne
    private ForgotPassword forgotPassword;

    public User() {
    }

    @Builder
    public User(String email, int score, String firstName, String lastName, String password, double weight,
            University university) {
        this.email = email;
        this.score = score;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.weight = weight;
        this.university = university;
    }

    public String getEmail() {
        return email;
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

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
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

}
