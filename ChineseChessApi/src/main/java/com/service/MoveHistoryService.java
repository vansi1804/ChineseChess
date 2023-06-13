package com.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.MatchCreationResponseDTO;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;

public interface MoveHistoryService {
    List<MoveHistoryDTO> findAllByMatchId(long matchId);

    boolean[][] findMoveValid(long matchId, int pieceId);

    @Transactional
    MatchCreationResponseDTO create(MoveHistoryCreationDTO moveHistoryCreationDTO);

}
