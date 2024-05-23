package com.pvt152.StudentLoppet.dto;

import com.pvt152.StudentLoppet.model.ProfilePicture;

public class UserScoreDTO {
    private String userName;
    private String email;
    private int score;
    private ProfilePicture profilePicture;

    public UserScoreDTO(String userName, String email, int score, ProfilePicture profilePicture) {
        this.userName = userName;
        this.email = email;
        this.score = score;
        this.profilePicture = profilePicture;
    }

    public UserScoreDTO(String userName, int score) {
        this.userName = userName;
        this.score = score;
        //this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "UserScoreDTO{" +
                "userName='" + userName + '\'' +
                ", score=" + score +
                ", profilePicture=" + (profilePicture != null ? profilePicture.getId() : "null") +
                '}';
    }

}