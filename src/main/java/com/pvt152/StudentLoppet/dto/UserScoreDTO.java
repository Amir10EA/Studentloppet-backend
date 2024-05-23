package com.pvt152.StudentLoppet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pvt152.StudentLoppet.model.ProfilePicture;

public class UserScoreDTO {
    private String userName;
    private String email;
    private int score;
    @JsonIgnore
    private ProfilePicture profilePicture;
    private String profilePictureBase64;

    public UserScoreDTO(String userName, String email, int score, ProfilePicture profilePicture) {
        this.userName = userName;
        this.email = email;
        this.score = score;
        this.profilePicture = profilePicture;
        this.profilePictureBase64 = profilePicture != null ? profilePicture.getImageAsBase64() : null;
    }
    public UserScoreDTO(String userName, int score, ProfilePicture profilePicture) {
        this(userName, null, score, profilePicture); // Call the primary constructor with email as null
    }

    // Getters and Setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return "UserScoreDTO{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", profilePictureBase64='" + (profilePictureBase64 != null ? "exists" : "null") + '\'' +
                '}';
    }
}
