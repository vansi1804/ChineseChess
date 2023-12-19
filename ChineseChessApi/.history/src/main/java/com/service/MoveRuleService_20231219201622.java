package com.service;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface MoveRuleService {
  boolean isValid(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    int toCol,
    int toRow
  );
}
