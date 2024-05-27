package com.pvt152.StudentLoppet.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.model.MidnattsloppRunner;

public interface MidnattsloppRunnerRepository extends CrudRepository<MidnattsloppRunner, String> {

    @Query("SELECT r FROM MidnattsloppRunner r WHERE TRIM(LOWER(r.name)) = TRIM(LOWER(:name)) AND r.yearBorn = :yearBorn")
    Optional<MidnattsloppRunner> findByNameAndYearBorn(@Param("name") String name, @Param("yearBorn") int yearBorn);
}