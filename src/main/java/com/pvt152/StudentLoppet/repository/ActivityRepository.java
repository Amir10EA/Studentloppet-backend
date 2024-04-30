package com.pvt152.StudentLoppet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT SUM(a.distance) as totalDistance, SUM(a.duration) as totalDuration FROM Activity a WHERE a.user.email = :email")
    Object[] findTotalDistanceAndDurationByEmail(@Param("email") String email);
}