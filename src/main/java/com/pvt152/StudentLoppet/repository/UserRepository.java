package com.pvt152.StudentLoppet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.model.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends CrudRepository<User, String> {

    // sparar resultatet från querien i "findScoresByUniversity" listan där varje
    // element av listan innehåller en egen lista av två element där första platsen
    // är universitet enum och andra det korresponderade poängen.
    @Query("SELECT u.university as university, SUM(u.score) as totalScore FROM User u WHERE u.university IS NOT NULL GROUP BY u.university ORDER BY SUM(u.score) DESC")
    List<Object[]> findScoresByUniversity();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
}
