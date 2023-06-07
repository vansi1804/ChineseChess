package com.service;

import com.data.dto.PlayBoardDTO;
import com.data.dto.PieceDTO;

public interface MovingRuleService {
    boolean isMoveValid(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow);

}
