package com.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.MoveHistoryCreationResponseDTO;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.TrainingMoveHistoryCreationDTO;
import com.data.dto.ValidMoveRequestDTO;
import com.data.entity.MoveHistory;

public interface MoveHistoryService {

    List<MoveHistoryDTO> build(List<MoveHistory> moveHistories);

    List<int[]> findMoveValid(ValidMoveRequestDTO validMoveRequestDTO);

    @Transactional
    MoveHistoryCreationResponseDTO create(TrainingMoveHistoryCreationDTO trainingMoveHistoryCreationDTO);

    @Transactional
    MoveHistoryCreationResponseDTO create(MoveHistoryCreationDTO moveHistoryCreationDTO);

}
