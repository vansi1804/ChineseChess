package com.service;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface MoveDescriptionService {
  String build(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    int toCol,
    int toRow
  );
}
