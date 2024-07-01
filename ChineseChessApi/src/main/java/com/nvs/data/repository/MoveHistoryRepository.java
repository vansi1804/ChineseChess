package com.nvs.data.repository;

import com.nvs.data.entity.MoveHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Long>{

   List<MoveHistory> findAllByMatch_Id(long matchId);

   long countTurnByMatch_Id(long matchId);

   List<MoveHistory> findAllByTraining_Id(long trainingId);

   long countTurnByTraining_Id(long trainingId);

}
