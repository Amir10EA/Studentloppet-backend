package com.pvt152.StudentLoppet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pvt152.StudentLoppet.model.ProfilePicture;
public class UserStats {
    private String fullName;
    private double value;
    @JsonIgnore
    private ProfilePicture profilePicture;
    private String profilePictureBase64;
    public UserStats(String fullName, double value, ProfilePicture profilePicture) {
        this.fullName = fullName;
        this.value = value;
        this.profilePicture = profilePicture;
        this.profilePictureBase64 = profilePicture != null ? profilePicture.getImageAsBase64() : null;
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
        this.profilePictureBase64 = profilePicture != null ? profilePicture.getImageAsBase64() : null;
    }

    public String getProfilePictureBase64() {
        return profilePictureBase64;
    }

    public void setProfilePictureBase64(String profilePictureBase64) {
        this.profilePictureBase64 = profilePictureBase64;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "fullName='" + fullName + '\'' +
                ", value=" + value +
                ", profilePictureBase64='" + (profilePictureBase64 != null ? "exists" : "null") + '\'' +
                '}';
    }
}
