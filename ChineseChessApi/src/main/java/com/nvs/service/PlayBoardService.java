package com.nvs.service;

import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.data.entity.MoveHistory;
import java.util.List;

public interface PlayBoardService {
  PlayBoardDTO generate();

  PlayBoardDTO build(List<MoveHistory> moveHistories);

  PlayBoardDTO update(
      PlayBoardDTO playBoardDTO,
      PieceDTO movingPieceDTO,
      int toCol,
      int toRow);

  void printTest(Object title, PlayBoardDTO playBoardDTO, PieceDTO pieceDTO);

  void printTest(
      PlayBoardDTO playBoardDTO,
      PieceDTO pieceDTO,
      List<int[]> availableMoveIndexes);

  boolean isGeneralInSafe(PlayBoardDTO playBoardDTO, PieceDTO generalPieceDTO);

  boolean areTwoGeneralsFacing(
      PlayBoardDTO playBoardDTO,
      PieceDTO generalPieceDTO1,
      PieceDTO generalPieceDTO2);

  boolean isGeneralBeingChecked(
      PlayBoardDTO playBoardDTO,
      PieceDTO generalPieceDTO);

  int evaluate(PlayBoardDTO playBoardDTO);
}
