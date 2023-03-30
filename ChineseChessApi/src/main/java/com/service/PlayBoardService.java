package com.service;

import com.data.dto.PieceDTO;
import com.data.entity.MoveHistory;

public interface PlayBoardService {
    PieceDTO[][] create();
    
    PieceDTO[][] update(PieceDTO[][] currentBoard, MoveHistory moveHistory);
}
