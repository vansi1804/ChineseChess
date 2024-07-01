package com.nvs.data.repository;

import com.nvs.data.entity.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
   @Query("SELECT m FROM Match m" +
         " WHERE m.result != NULL " +
         "      AND (m.player1.user.id = :playerId OR m.player2.user.id = :playerId)")
   List<Match> findAllByPlayerId(@Param("playerId") long playerId);

   @Query("SELECT CASE" +
         "            WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END" +
         " FROM Match m" +
         " WHERE m.result IS NULL" +
         "      AND (m.player1.id = :playerId OR m.player2.id = :playerId)")
   boolean existsPlayingByPlayerId(@Param("playerId") long playerId);

}
