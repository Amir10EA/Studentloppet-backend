package com.pvt152.StudentLoppet.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Activity {
    private static final Logger logger = Logger.getLogger(Activity.class.getName());
    private static final double CALORIES_PER_MINUTE_CONSTANT = 0.0175;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double distance;
    private long duration;
    private LocalDateTime timestamp;
    private double caloriesBurned;
    private int scoreGained;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    public Activity() {
    }

    public Activity(double distance, long duration, User user, int scoreGained) {
        this.distance = distance;
        this.duration = duration;
        this.user = user;
        this.scoreGained = scoreGained;
        this.timestamp = LocalDateTime.now();

        if (user != null && user.getWeight() > 0) {
            this.caloriesBurned = calculateCaloriesBurned(distance, duration, user.getWeight()).doubleValue();
        } else {
            this.caloriesBurned = 0;
        }
    }

    private BigDecimal calculateCaloriesBurned(double distanceInKm, long durationInMinutes, double weightInKg) {
        if (distanceInKm <= 0 || durationInMinutes <= 0 || weightInKg <= 0) {
            return BigDecimal.ZERO;
        }

        double durationInHours = durationInMinutes / 60.0;
        double speedInKph = distanceInKm / durationInHours;

        logger.info("Distance in km: " + distanceInKm);
        logger.info("Duration in minutes: " + durationInMinutes);
        logger.info("Duration in hours: " + durationInHours);
        logger.info("Speed in km/h: " + speedInKph);

        double met = calculateMetValue(speedInKph);

        logger.info("MET value: " + met);

        double caloriesBurnedPerMinute = CALORIES_PER_MINUTE_CONSTANT * met * weightInKg;
        double totalCaloriesBurned = caloriesBurnedPerMinute * durationInMinutes;

        logger.info("Calories burned: " + totalCaloriesBurned);

        return BigDecimal.valueOf(totalCaloriesBurned).setScale(2, RoundingMode.HALF_UP);
    }

    private double calculateMetValue(double speedInKph) {
        if (speedInKph < 6) {
            return interpolateMet(0, 6, 1.0, 6.0, speedInKph);
        } else if (speedInKph < 8) {
            return interpolateMet(6, 8, 6.0, 8.0, speedInKph);
        } else if (speedInKph < 10) {
            return interpolateMet(8, 10, 8.0, 9.5, speedInKph);
        } else if (speedInKph < 12) {
            return interpolateMet(10, 12, 9.5, 11.0, speedInKph);
        } else if (speedInKph < 14) {
            return interpolateMet(12, 14, 11.0, 12.5, speedInKph);
        } else {
            return interpolateMet(14, 16, 12.5, 15.0, speedInKph);
        }
    }

    private double interpolateMet(double speedMin, double speedMax, double metMin, double metMax, double speedInKph) {
        return metMin + (metMax - metMin) * ((speedInKph - speedMin) / (speedMax - speedMin));
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
