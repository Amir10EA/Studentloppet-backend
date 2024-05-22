package com.pvt152.StudentLoppet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.pvt152.StudentLoppet.model.RunnerResult;

@Repository
public interface MidnattsloppResultRepository extends CrudRepository<RunnerResult, String> {
}