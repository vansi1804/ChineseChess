package com.service;

import com.data.dto.PlayBoardDTO;

import com.data.dto.PieceDTO;

public interface MoveRuleService {

    boolean checkMoveRule(PlayBoardDTO playBoard, PieceDTO pieceDTO, int toCol, int toRow);

    boolean isMoveValid(PlayBoardDTO playBoard, PieceDTO pieceDTO, int toCol, int toRow);

    boolean isGeneralBeingChecked(PlayBoardDTO playBoard, PieceDTO generalPiece);
}
