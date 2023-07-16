package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MovedResponseDTO;
import com.data.dto.move.BestMoveDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.ValidMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveHistoryService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findAllAvailableMoves(ValidMoveRequestDTO validMoveRequestDTO);

    MovedResponseDTO create(MoveCreationDTO moveCreationDTO);

    @Transactional
    MovedResponseDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MovedResponseDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

    List<BestMoveDTO> findAllBestMoves(PlayBoardDTO playBoardDTO, int depth);

}
