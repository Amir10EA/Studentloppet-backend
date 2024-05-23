package com.pvt152.StudentLoppet.dto;

import com.pvt152.StudentLoppet.model.ProfilePicture;

public class UserStats {
    private String fullName;
    private double value;
    private ProfilePicture profilePicture;

    public UserStats(String fullName, double value, ProfilePicture profilePicture) {
        this.fullName = fullName;
        this.value = value;
        this.profilePicture = profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public double getValue() {
        return value;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "userName='" + fullName + '\'' +
                ", value=" + value +
                ", profilePicture=" + (profilePicture != null ? profilePicture.getId() : "null") +
                '}';
    }

}