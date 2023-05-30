package com.service;

import java.util.List;

import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;

public interface MoveHistoryService {
    List<MoveHistoryDTO> findAllByMatchId(long matchId);

    List<PieceDTO> create(MoveHistoryCreationDTO moveHistoryCreationDTO);

}
