package com.pvt152.StudentLoppet.dto;

import com.pvt152.StudentLoppet.model.University;

public class UniversityScoreDTO {
    private String universityDisplayName;
    private int score;

    public UniversityScoreDTO(String universityDisplayName, int score) {
        this.universityDisplayName = universityDisplayName;
        this.score = score;
    }

    public String getUniversityDisplayName() {
        return universityDisplayName;
    }

    public void setUniversityDisplayName(String universityDisplayName) {
        this.universityDisplayName = universityDisplayName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}