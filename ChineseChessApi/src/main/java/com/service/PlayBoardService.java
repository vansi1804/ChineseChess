package com.service;

import com.data.dto.PieceDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;

public interface PlayBoardService {
    PieceDTO[][] create();
    
    PieceDTO[][] update(Piece[][] currentBoard, MoveHistory moveHistory);
}
