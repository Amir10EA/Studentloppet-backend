package com.pvt152.StudentLoppet;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String university;

    private LocalDateTime registrationDate;
    private LocalDateTime lastActiveDate;

    private Integer intervalRegistrationDate;
    private Integer intervalLastActiveDate;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return this.university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public LocalDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastActiveDate() {
        return this.lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public Integer getIntervalRegistrationDate() {
        return this.intervalRegistrationDate;
    }

    public void setIntervalRegistrationDate(Integer intervalRegistrationDate) {
        this.intervalRegistrationDate = intervalRegistrationDate;
    }

    public Integer getintervalLastActiveDate() {
        return this.intervalLastActiveDate;
    }

    public void setIntervalLastActiveDate(Integer intervalLastActiveDate) {
        this.intervalLastActiveDate = intervalLastActiveDate;
    }

}
