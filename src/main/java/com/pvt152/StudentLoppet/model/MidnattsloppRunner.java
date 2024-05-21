package com.pvt152.StudentLoppet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MidnattsloppRunner {
    @Id
    private String startNumber;
    private String name;
    private int yearBorn;
    private String clubOrCityOrCompany;
    private String startGroup;

    // Constructors
    public MidnattsloppRunner(String startNumber, String name, int yearBorn, String clubOrCityOrCompany,
            String startGroup) {
        this.startNumber = startNumber;
        this.name = name;
        this.yearBorn = yearBorn;
        this.clubOrCityOrCompany = clubOrCityOrCompany;
        this.startGroup = startGroup;
    }

    public MidnattsloppRunner() {
    }

    // Getters and setters
    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearBorn() {
        return yearBorn;
    }

    public void setYearBorn(int yearBorn) {
        this.yearBorn = yearBorn;
    }

    public String getClubOrCityOrCompany() {
        return clubOrCityOrCompany;
    }

    public void setClubOrCityOrCompany(String clubOrCityOrCompany) {
        this.clubOrCityOrCompany = clubOrCityOrCompany;
    }

    public String getStartGroup() {
        return startGroup;
    }

    public void setStartGroup(String startGroup) {
        this.startGroup = startGroup;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MidnattsloppRunner that = (MidnattsloppRunner) o;
        return startNumber != null ? startNumber.equals(that.startNumber) : that.startNumber == null;
    }

    @Override
    public int hashCode() {
        return startNumber != null ? startNumber.hashCode() : 0;
    }

    // ToString method
    @Override
    public String toString() {
        return "MidnattsloppRunner{" +
                "startNumber='" + startNumber + '\'' +
                ", name='" + name + '\'' +
                ", yearBorn=" + yearBorn +
                ", clubOrCityOrCompany='" + clubOrCityOrCompany + '\'' +
                ", startGroup='" + startGroup + '\'' +
                '}';
    }
}