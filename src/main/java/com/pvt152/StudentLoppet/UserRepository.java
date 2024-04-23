package com.pvt152.StudentLoppet;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pvt152.StudentLoppet.model.User;

public interface UserRepository extends CrudRepository<User, String> {

    // sparar resultatet från querien i "findScoresByUniversity" listan där varje
    // element av listan innehåller en egen lista av två element där första platsen
    // är universitet enum och andra det korresponderade poängen.
    @Query("SELECT u.university as university, SUM(u.score) as totalScore FROM User u GROUP BY u.university ORDER BY SUM(u.score) DESC")
    List<Object[]> findScoresByUniversity();

}
