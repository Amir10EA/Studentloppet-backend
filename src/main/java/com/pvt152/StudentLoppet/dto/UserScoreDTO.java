package com.pvt152.StudentLoppet.dto;

public class UserScoreDTO {
    private String userName;
    private String email;
    private int score;

    public UserScoreDTO(String userName, String email, int score) {
        this.userName = userName;
        this.email = email;
        this.score = score;
    }

    public UserScoreDTO(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    // Getters and setter
    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    // Setters
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

    @Override
    public String toString() {
        return "UserScoreDTO{" +
                "userName='" + userName + '\'' +
                ", score=" + score +
                '}';
    }

}
