package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.EIndexDescription;
import com.nvs.common.enumeration.EMoveTypeDescription;
import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.service.MoveDescriptionService;
import com.nvs.service.MoveTypeService;
import com.nvs.service.PieceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveDescriptionServiceImpl implements MoveDescriptionService {

  private final PieceService pieceService;
  private final MoveTypeService moveTypeService;

  @Override
  public String build(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
    String shortName = pieceService.convertByName(pieceDTO.getName()).getShortName();

    Character indexChar = buildIndexDescription(playBoardDTO, pieceDTO);
    String index = indexChar != null ? indexChar.toString() : "";

    int MAX_COL = Default.Game.PlayBoardSize.COL_MAX;
    int currentPosition =
        pieceDTO.isRed() ? (MAX_COL - pieceDTO.getCurrentCol() + 1) : pieceDTO.getCurrentCol() + 1;
    char moveType;
    int targetPosition;

    if (moveTypeService.isHorizontallyMoving(pieceDTO.getCurrentRow(), toRow)) {
      moveType = EMoveTypeDescription.ACROSS.getValue();
      targetPosition = pieceDTO.isRed() ? (MAX_COL - toCol + 1) : toCol + 1;
    } else {
      moveType = moveTypeService.isUpMoving(pieceDTO.isRed(), pieceDTO.getCurrentRow(), toRow)
          ? EMoveTypeDescription.UP.getValue() : EMoveTypeDescription.DOWN.getValue();

      targetPosition =
          moveTypeService.isVerticallyMoving(pieceDTO.getCurrentCol(), toCol) ? Math.abs(
              pieceDTO.getCurrentRow() - toRow)
              : (pieceDTO.isRed() ? (MAX_COL - toCol + 1) : toCol + 1);
    }

    return shortName + index + currentPosition + moveType + targetPosition;
  }

  private Character buildIndexDescription(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
    PieceDTO foundPiece = pieceService.findExistingTheSameInColPath(playBoardDTO, pieceDTO);

    if (foundPiece == null) {
      return null;
    } else {
      boolean isRowBefore =
          (pieceDTO.isRed() && (pieceDTO.getCurrentRow() < foundPiece.getCurrentRow())) || (
              !pieceDTO.isRed() && (pieceDTO.getCurrentRow() > foundPiece.getCurrentRow()));

      return isRowBefore ? EIndexDescription.BEFORE.getValue() : EIndexDescription.AFTER.getValue();
    }
  }

}
