package com.service;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface MoveDescriptionService {
    String build(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow);
}
