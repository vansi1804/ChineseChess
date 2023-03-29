package com.service;

import com.data.entity.MoveHistory;
import com.data.entity.Piece;

public interface MoveDescriptionService {
    String getDescription(Piece[][] currentBoard, MoveHistory moveHistory);
}
