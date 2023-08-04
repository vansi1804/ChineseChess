package com.service;

import com.data.dto.PlayBoardDTO;

import com.data.dto.PieceDTO;

public interface MoveRuleService {

    boolean isValid(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow);

}
