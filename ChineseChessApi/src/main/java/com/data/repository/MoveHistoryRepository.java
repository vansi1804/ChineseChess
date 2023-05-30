package com.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.data.entity.MoveHistory;

public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Long> {

        List<MoveHistory> findAllByMatch_Id(@Param("matchId") long matchId);

        @Query("SELECT lmh FROM MoveHistory lmh"
                        + " WHERE lmh.match.id = :matchId"
                        + "     AND lmh.turn = (SELECT MAX(mh.turn) FROM MoveHistory mh"
                        + "                     WHERE mh.match.id = :matchId"
                        + "                             AND mh.turn <= :turn"
                        + "                             AND mh.toCol = :colMovingTo"
                        + "                             AND mh.toRow = :rowMovingTo"
                        + "                     ORDER BY mh.turn DESC)")
        Optional<MoveHistory> findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(
                        @Param("matchId") long matchId, @Param("turn") long turn,
                        @Param("colMovingTo") int colMovingTo, @Param("rowMovingTo") int rowMovingTo);

        long countTurnByMatch_Id(long matchId);
}
