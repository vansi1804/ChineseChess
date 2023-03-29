package com.service;

import java.util.List;

import com.data.dto.GameViewDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;

public interface MoveHistoryService {
    List<GameViewDTO> findAllByMatchId(long matchId);

    List<PieceDTO> create(MoveHistoryDTO moveHistoryDTO);

}
