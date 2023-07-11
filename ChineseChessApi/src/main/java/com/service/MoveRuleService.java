package com.service;

import com.data.dto.PlayBoardDTO;

import com.data.dto.PieceDTO;

public interface MoveRuleService {

    boolean isValidMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow);

    boolean isAvailableMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow);

    boolean isGeneralBeingChecked(PlayBoardDTO playBoardDTO, PieceDTO generalPiece);
    
}
