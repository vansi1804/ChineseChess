package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveCreationResponseDTO;
import com.data.dto.move.BestMoveDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.ValidMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveHistoryService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findAllAvailableMoves(ValidMoveRequestDTO validMoveRequestDTO);

    MoveCreationResponseDTO create(MoveCreationDTO moveCreationDTO);

    @Transactional
    MoveCreationResponseDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MoveCreationResponseDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

    List<BestMoveDTO> findAllBestMoves(PlayBoardDTO playBoardDTO, int depth);

}
