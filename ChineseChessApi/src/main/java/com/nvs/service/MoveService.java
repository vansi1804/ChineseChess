package com.nvs.service;

import com.nvs.data.dto.move.MoveCreationDTO;
import com.nvs.data.dto.move.MoveDTO;
import com.nvs.data.dto.move.MoveHistoryDTO;
import com.nvs.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.nvs.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.nvs.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import java.util.List;
import java.util.Map;

public interface MoveService {
  Map<Long, MoveHistoryDTO> findAllByMatchId(long matchId);

  Map<Long, MoveHistoryDTO> findAllByTrainingId(long trainingId);

  List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO);

  MoveDTO create(MoveCreationDTO moveCreationDTO);

  MoveDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

  MoveDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

  Map<Boolean, BestAvailableMoveResponseDTO> findAllBestAvailable(
      BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO);
}
