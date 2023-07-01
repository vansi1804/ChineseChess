package com.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.entity.MoveHistory;

@Repository
public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Long> {

        List<MoveHistory> findAllByMatch_Id(long matchId);

        @Query(value = "SELECT mh.* FROM move_histories mh "
                        + "WHERE mh.match_id = :matchId "
                        + "AND mh.turn <= :turn "
                        + "AND mh.to_col = :col "
                        + "AND mh.to_row = :row "
                        + "ORDER BY mh.turn DESC "
                        + "LIMIT 1", nativeQuery = true)
        Optional<MoveHistory> findLastByMatchIdAndPositionUntilTurn(
                        @Param("matchId") long matchId, @Param("turn") long turn,
                        @Param("col") int col, @Param("row") int row);

        long countTurnByMatch_Id(long matchId);

        Optional<MoveHistory> findByMatch_idAndTurn(long matchId, long turn);

        @Query(value = "SELECT mh.to_col, mh.to_row FROM move_histories mh "
                        + "WHERE mh.match_id = :matchId "
                        + "AND mh.piece_id = :pieceId "
                        + "ORDER BY mh.turn DESC "
                        + "LIMIT 1", nativeQuery = true)
        int[] findLastPositionByMatchIdAndPieceId(
                        @Param("matchId") long matchId, @Param("pieceId") int pieceId);

        // =========================================================

        List<MoveHistory> findAllByTraining_Id(long trainingId);

        long countTurnByTraining_Id(long trainingId);

        @Query(value = "SELECT mh.* FROM move_histories mh "
                        + "WHERE mh.training_id = :trainingId "
                        + "AND mh.turn <= :turn "
                        + "AND mh.to_col = :col "
                        + "AND mh.to_row = :row "
                        + "ORDER BY mh.turn DESC "
                        + "LIMIT 1", nativeQuery = true)
        Optional<MoveHistory> findLastByTrainingIdAndPositionUntilTurn(
                        @Param("trainingId") long trainingId, @Param("turn") long turn,
                        @Param("col") int col, @Param("row") int row);

        Optional<MoveHistory> findByTraining_idAndTurn(long trainingId, long turn);

        @Query(value = "SELECT mh.to_col, mh.to_row FROM move_histories mh "
                        + "WHERE mh.training_id = :trainingId "
                        + "AND mh.piece_id = :pieceId "
                        + "ORDER BY mh.turn DESC "
                        + "LIMIT 1", nativeQuery = true)
        int[] findLastPositionByTrainingIdAndPieceId(
                        @Param("trainingId") long trainingId, @Param("pieceId") int pieceId);

        void deleteAllByTraining_Id(long trainingId);

}
