package com.pvt152.StudentLoppet;

public class UniversityScoreDTO {
    private University university;
    private int score;

    public UniversityScoreDTO(University university, int score) {
        this.university = university;
        this.score = score;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}