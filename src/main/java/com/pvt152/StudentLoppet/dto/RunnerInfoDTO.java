package com.pvt152.StudentLoppet.dto;

public class RunnerInfoDTO {
    private boolean registered;
    private String startNumber;
    private String clubOrCityOrCompany;
    private String startGroup;

    public RunnerInfoDTO(boolean registered, String startNumber, String clubOrCityOrCompany, String startGroup) {
        this.registered = registered;
        this.startNumber = startNumber;
        this.clubOrCityOrCompany = clubOrCityOrCompany;
        this.startGroup = startGroup;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
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
}