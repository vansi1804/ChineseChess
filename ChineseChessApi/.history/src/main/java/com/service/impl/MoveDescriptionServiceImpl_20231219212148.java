package com.service.impl;

import com.common.Default;
import com.common.enumeration.EIndexDescription;
import com.common.enumeration.EMoveTypeDescription;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveDescriptionService;
import com.service.MoveTypeService;
import com.service.PieceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveDescriptionServiceImpl implements MoveDescriptionService {

  private final int MAX_COL = Default.Game.PlayBoardSize.COL_MAX;
  private final PieceService pieceService;
  private final MoveTypeService moveTypeService;

  @Override
  public String build(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    int toCol,
    int toRow
  ) {
    String shortName = pieceService
      .convertByName(pieceDTO.getName())
      .getShortName();
    Character index = buildIndexDescription(playBoardDTO, pieceDTO);
    int currentPosition = pieceDTO.isRed()
      ? (MAX_COL - pieceDTO.getCurrentCol())
      : pieceDTO.getCurrentCol();
    char moveType;
    int targetPosition;

    if (moveTypeService.isHorizontallyMoving(pieceDTO.getCurrentRow(), toRow)) {
      moveType = EMoveTypeDescription.ACROSS.getValue();
      targetPosition = pieceDTO.isRed() ? (MAX_COL - toCol) : toCol;
    } else {
      moveType =
        moveTypeService.isUpMoving(
            pieceDTO.isRed(),
            pieceDTO.getCurrentRow(),
            toRow
          )
          ? EMoveTypeDescription.UP.getValue()
          : EMoveTypeDescription.DOWN.getValue();

      targetPosition =
        moveTypeService.isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)
          ? Math.abs(pieceDTO.getCurrentRow() - toRow)
          : (pieceDTO.isRed() ? (MAX_COL - toCol) : toCol);
    }

    return new StringBuilder()
      .append(shortName)
      .append(index)
      .append(currentPosition)
      .append(moveType)
      .append(targetPosition)
      .toString();
  }

  private Character buildIndexDescription(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO
  ) {
    PieceDTO foundPiece = pieceService.findExistingTheSameInColPath(
      playBoardDTO,
      pieceDTO
    );

    if (foundPiece == null) {
      return null;
    } else {
      boolean isRowBefore =
        (
          pieceDTO.isRed() &&
          (pieceDTO.getCurrentRow() < foundPiece.getCurrentRow())
        ) ||
        (
          !pieceDTO.isRed() &&
          (pieceDTO.getCurrentRow() > foundPiece.getCurrentRow())
        );

      return isRowBefore
        ? EIndexDescription.BEFORE.getValue()
        : EIndexDescription.AFTER.getValue();
    }
  }
}
