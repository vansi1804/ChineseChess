package com.service;

import com.data.dto.PlayBoardDTO;

import com.data.dto.PieceDTO;

public interface MovingRuleService {
    boolean isMoveValid(PlayBoardDTO playBoard, PieceDTO pieceDTO, int toCol, int toRow);

    boolean areTwoGeneralsFacing(PlayBoardDTO playBoard, PieceDTO generalPiece1, PieceDTO generalPiece2);

    boolean isGeneralBeingChecked(PlayBoardDTO playBoard, PieceDTO generalPiece);
}
