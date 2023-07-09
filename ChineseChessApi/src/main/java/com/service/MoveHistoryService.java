package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.MovedResponseDTO;
import com.data.dto.MatchMoveCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.TrainingMoveCreationDTO;
import com.data.dto.ValidMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveHistoryService {

    Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findMoveValid(ValidMoveRequestDTO validMoveRequestDTO);

    @Transactional
    MovedResponseDTO create(TrainingMoveCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MovedResponseDTO create(MatchMoveCreationDTO moveHistoryCreationDTO);

}
