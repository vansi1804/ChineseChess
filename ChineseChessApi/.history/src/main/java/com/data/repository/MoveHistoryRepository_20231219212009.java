package com.data.repository;

import com.data.entity.MoveHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveHistoryRepository
  extends JpaRepository<MoveHistory, Long> {
  List<MoveHistory> findAllByMatch_Id(long matchId);

  Optional<MoveHistory> findFirstByMatch_IdAndToColAndToRowAndTurnLessThanEqualOrderByTurnDesc(
    long matchId,
    int toCol,
    int toRow,
    long turn
  );

  long countTurnByMatch_Id(long matchId);

  Optional<MoveHistory> findByMatch_idAndTurn(long matchId, long turn);

  List<MoveHistory> findAllByTraining_Id(long trainingId);

  long countTurnByTraining_Id(long trainingId);

  Optional<MoveHistory> findFirstByTraining_IdAndToColAndToRowAndTurnLessThanEqualOrderByTurnDesc(
    long trainingId,
    int toCol,
    int toRow,
    long turn
  );

  Optional<MoveHistory> findByTraining_idAndTurn(long trainingId, long turn);

  void deleteAllByTraining_Id(long trainingId);
}
