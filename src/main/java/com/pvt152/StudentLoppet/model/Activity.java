package com.pvt152.StudentLoppet.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double distance; // in kilometers
    private long duration; // in minutes
    private LocalDateTime timestamp; // when the run was done
    private double caloriesBurned;
    private int scoreGained;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    // Default constructor
    public Activity() {
    }

    public Activity(double distance, long duration, User user, int scoreGained) {
        this.distance = distance;
        this.duration = duration;
        this.user = user;
        this.scoreGained = scoreGained;
        // Calculate calories burned if weight is provided
        if (user != null && user.getWeight() > 0) {
            this.caloriesBurned = calculateCaloriesBurned(distance, duration, user.getWeight());
        } else {
            this.caloriesBurned = 0; // Default to 0 calories if weight is not set
        }
    }

    private double calculateCaloriesBurned(double distanceInKm, long durationInMinutes, double weightInKg) {
        double distanceInMeters = distanceInKm * 1000; // Convert km to meters
        double durationInHours = durationInMinutes / 60.0; // Convert minutes to hours
        double speedInMetersPerSecond = distanceInMeters / (durationInHours * 3600); // Calculate speed in m/s
        double speedInKph = speedInMetersPerSecond * 3.6;

        // Determine MET value based on speed
        double met;
        if (speedInKph < 8) {
            met = 8.3; // Light running
        } else if (speedInKph < 12) {
            met = 9.8; // Moderate running
        } else {
            met = 11.0; // Fast running
        }

        // Calculate calories burned
        return met * weightInKg * durationInHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScoreGained() {
        return scoreGained;
    }

    public void setScoreGained(int scoreGained) {
        this.scoreGained = scoreGained;
    }

}