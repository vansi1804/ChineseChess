package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveCreationResponseDTO;
import com.data.dto.move.BestMoveRequestDTO;
import com.data.dto.move.BestMoveResponseDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.ValidMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findAllAvailableMoves(ValidMoveRequestDTO validMoveRequestDTO);

    MoveCreationResponseDTO create(MoveCreationDTO moveCreationDTO);

    @Transactional
    MoveCreationResponseDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MoveCreationResponseDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

    List<BestMoveResponseDTO> findAllBestMoves(BestMoveRequestDTO bestMoveRequestDTO);

}
