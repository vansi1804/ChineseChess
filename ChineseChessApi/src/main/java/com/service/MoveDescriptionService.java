package com.service;

import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;

public interface MoveDescriptionService {
    String getDescription(PlayBoardDTO currentBoard, MoveHistory moveHistory);
}
