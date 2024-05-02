package com.pvt152.StudentLoppet.dto;

public class UserStats {
    private String fullName;
    private double value;

    public UserStats(String fullName, double value) {
        this.fullName = fullName;
        this.value = value;
    }

    public String getFullName() {
        return fullName;
    }

    public double getValue() {
        return value;
    }

}