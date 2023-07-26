package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.BestMoveRequestDTO;
import com.data.dto.move.BestMoveResponseDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.AvailableMoveRequest;
import com.data.entity.MoveHistory;

public interface MoveService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findAllAvailableMoves(AvailableMoveRequest validMoveRequestDTO);

    MoveDTO create(MoveCreationDTO moveCreationDTO);

    @Transactional
    MoveDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MoveDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

    List<BestMoveResponseDTO> findAllBestMoves(BestMoveRequestDTO bestMoveRequestDTO);

}
