package com.service;

import java.util.List;
import java.util.Map;

import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.BestAvailableMoveRequestDTO;
import com.data.dto.move.BestMoveResponseDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.AvailableMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO);

    MoveDTO create(MoveCreationDTO moveCreationDTO);

    MoveDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    MoveDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

    List<BestMoveResponseDTO> findAllBestAvailable(BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO);

}
