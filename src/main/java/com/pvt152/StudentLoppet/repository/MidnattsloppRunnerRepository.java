package com.pvt152.StudentLoppet.repository;

import org.springframework.data.repository.CrudRepository;

import com.pvt152.StudentLoppet.model.MidnattsloppRunner;

public interface MidnattsloppRunnerRepository extends CrudRepository<MidnattsloppRunner, String> {
    // You can define custom database queries here if needed
}