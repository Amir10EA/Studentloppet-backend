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

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    // Default constructor
    public Activity() {
    }

    public Activity(double distance, long duration, User user) {
        this.distance = distance;
        this.duration = duration;
        this.user = user;
        // Check if weight and height are provided
        if (user != null && user.getWeight() > 0 && user.getHeight() > 0) {
            this.caloriesBurned = calculateCaloriesBurned(distance, duration, user.getWeight(), user.getHeight());
        } else {
            this.caloriesBurned = 0; // Default to 0 calories if weight or height are not set
        }
    }

    private double calculateCaloriesBurned(double distanceInKm, long durationInMinutes, double weightInKg,
            double heightInM) {
        double distanceInMeters = distanceInKm * 1000; // Convert km to meters
        double durationInHours = durationInMinutes / 60.0; // Convert minutes to hours
        double speedInMetersPerSecond = distanceInMeters / (durationInHours * 3600); // Calculate speed in m/s

        // Calories calculation formula
        double caloriesPerMinute = 0.035 * weightInKg
                + (Math.pow(speedInMetersPerSecond, 2) / heightInM) * 0.029 * weightInKg;

        return caloriesPerMinute * durationInMinutes;
    }

    // Getters and setters
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

}