package com.service;

import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.data.entity.MoveHistory;
import java.util.List;
import java.util.Map;

public interface MoveService {
  Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

  List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO);

  MoveDTO create(MoveCreationDTO moveCreationDTO);

  MoveDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

  MoveDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

  Map<Boolean, BestAvailableMoveResponseDTO> findAllBestAvailable(
    BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO
  );
}
