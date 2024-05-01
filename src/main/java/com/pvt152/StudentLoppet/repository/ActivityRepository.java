package com.pvt152.StudentLoppet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    List<Activity> findByUserEmail(String email);
}