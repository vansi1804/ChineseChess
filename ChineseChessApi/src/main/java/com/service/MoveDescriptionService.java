package com.service;

import com.data.dto.PieceDTO;
import com.data.entity.MoveHistory;

public interface MoveDescriptionService {
    String getDescription(PieceDTO[][] currentBoard, MoveHistory moveHistory);
}
