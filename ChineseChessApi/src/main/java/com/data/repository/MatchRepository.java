package com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {    
    @Query("SELECT m FROM Match m WHERE m.player1.user.id = :id OR m.player2.user.id = :id")
    List<Match> findAllByPlayerId(@Param("id") long id);

}
