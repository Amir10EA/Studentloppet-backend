package com.pvt152.StudentLoppet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RunnerResult {

    @Id
    private String id;
    private String name;
    private String organization;
    private String time;
    private String raceClass;
    private int yearOfBirth;
    private int place;

    public RunnerResult() {
    }

    public RunnerResult(String id, String name, String organization, String time, String raceClass, int yearOfBirth,
            int place) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.time = time;
        this.raceClass = raceClass;
        this.yearOfBirth = yearOfBirth;
        this.place = place;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getRaceClass() {
        return raceClass;
    }

    public void setRaceClass(String raceClass) {
        this.raceClass = raceClass;
    }
}