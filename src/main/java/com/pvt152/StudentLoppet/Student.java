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
    private String University;

    private LocalDateTime registrationDate;
    private LocalDateTime lastActiveDate;

    private Integer intervalRegistrationDate;
    private Integer intervalLastActiveDate;

    // ID
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // University
    public String getUniversity() {
        return University;
    }

    public void setUniversity(String university) {
        University = university;
    }

    // RegistrationDate
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    // LastActiveDate
    public LocalDateTime getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    // IntervalRegistrationDate
    public Integer getIntervalRegistrationDate() {
        return intervalRegistrationDate;
    }

    public void setIntervalRegistrationDate(Integer intervalRegistrationDate) {
        this.intervalRegistrationDate = intervalRegistrationDate;
    }

    // IntervalLastActiveDate
    public Integer getIntervalLastActiveDate() {
        return intervalLastActiveDate;
    }

    public void setIntervalLastActiveDate(Integer intervalLastActiveDate) {
        this.intervalLastActiveDate = intervalLastActiveDate;
    }
}