package com.pvt152.StudentLoppet.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.model.Activity;
import com.pvt152.StudentLoppet.model.University;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    List<Activity> findByUserEmail(String email);

    @Query("SELECT a FROM Activity a WHERE a.user.email = :email AND a.timestamp >= :startDate")
    List<Activity> findByUserEmailAndTimestampAfter(@Param("email") String email,
            @Param("startDate") LocalDateTime startDate);

    @Query("SELECT a FROM Activity a WHERE a.user.university = :university")
    List<Activity> findByUniversity(@Param("university") University university);

    @Query("SELECT a.user.university as university, SUM(a.distance) as totalDistance FROM Activity a GROUP BY a.user.university")
    List<Object[]> sumDistanceByUniversity();

    @Query("SELECT a.user.email, SUM(a.distance) FROM Activity a WHERE a.user.university = :university GROUP BY a.user.email ORDER BY SUM(a.distance) DESC")
    List<Object[]> findTotalDistanceByUniversity(@Param("university") University university);

    @Query("SELECT a.user.email, SUM(a.caloriesBurned) FROM Activity a WHERE a.user.university = :university GROUP BY a.user.email ORDER BY SUM(a.caloriesBurned) DESC")
    List<Object[]> findTotalCaloriesBurnedByUniversity(@Param("university") University university);

    @Query("SELECT a.user.email, AVG(a.distance / (a.duration / 60.0)) as averageSpeed FROM Activity a WHERE a.user.university = :university GROUP BY a.user.email ORDER BY averageSpeed DESC")
    List<Object[]> findAverageSpeedByUniversity(@Param("university") University university);

    @Query("SELECT a.user.email, SUM(a.distance) as totalDistance, SUM(a.duration) as totalDuration FROM Activity a WHERE a.user.university = :university GROUP BY a.user.email ORDER BY a.user.email")
    List<Object[]> findTotalDistanceAndDurationByUniversity(@Param("university") University university);

}
