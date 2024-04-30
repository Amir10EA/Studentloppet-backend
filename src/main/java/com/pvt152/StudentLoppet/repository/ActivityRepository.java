package com.pvt152.StudentLoppet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pvt152.StudentLoppet.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // Additional query methods can be defined here
}