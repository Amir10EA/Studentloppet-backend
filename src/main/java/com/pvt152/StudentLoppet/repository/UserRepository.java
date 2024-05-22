package com.pvt152.StudentLoppet.repository;

import java.util.List;

import com.pvt152.StudentLoppet.dto.UserScoreDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvt152.StudentLoppet.dto.UserStats;
import com.pvt152.StudentLoppet.model.University;
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

    @Query("SELECT u.score FROM User u WHERE u.email = :email")
    Integer findScoreByEmail(@Param("email") String email);

    @Query("SELECT u.university as university, COUNT(u) as userCount FROM User u WHERE u.university IS NOT NULL GROUP BY u.university ORDER BY COUNT(u) DESC")
    List<Object[]> countUsersByUniversity();

    @Query("SELECT u FROM User u WHERE u.university = :university")
    List<User> findByUniversity(@Param("university") University university);

    @Query("SELECT u.email, CONCAT(u.firstName, ' ', u.lastName), u.score FROM User u ORDER BY u.score DESC")
    List<Object[]> findScoresByUser();

    @Query("SELECT u.email, u.score FROM User u WHERE u.university = :university ORDER BY u.score DESC")
    List<Object[]> findScoresByUniversity(@Param("university") University university);

    User findByEmail(String email);



}

