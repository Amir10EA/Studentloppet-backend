package com.pvt152.StudentLoppet;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT u.university as university, SUM(u.score) as totalScore FROM User u GROUP BY u.university ORDER BY SUM(u.score) DESC")
    List<Object[]> findScoresByUniversity();

}
