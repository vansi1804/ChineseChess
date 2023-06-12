package com.service.impl;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.common.Default;
import com.common.enumeration.EIndex;
import com.common.enumeration.EMove;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveDescriptionService;
import com.service.PieceService;

@Service
public class MoveDescriptionServiceImpl implements MoveDescriptionService {
    private final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW;

    @Autowired
    private PieceService pieceService;

    @Override
    public String buildDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        String shortNameOfPiece = pieceService.convertByName(pieceDTO.getName()).getShortNameValue();

        StringBuilder description = new StringBuilder();
        description.append(shortNameOfPiece);

        if (pieceDTO.isRed()) {
            description.append(buildRedPieceDescription(currentBoard, pieceDTO, toCol, toRow));
        } else {
            description.append(buildBlackPieceDescription(currentBoard, pieceDTO, toCol, toRow));
        }

        return description.toString();
    }

    private String buildRedPieceDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(buildIndexDescription(currentBoard, pieceDTO));
        description.append(MAX_COL - pieceDTO.getCurrentCol() + 1);

        if (isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMove.ACROSS.getValue())
                    .append(MAX_COL - toCol + 1);

        } else {
            if (isUpMoving(true, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMove.UP.getValue());
            } else {
                description.append(EMove.DOWN.getValue());
            }

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(Math.abs(pieceDTO.getCurrentRow() - toRow));
            } else {
                description.append(MAX_COL - toCol + 1);
            }
        }

        return description.toString();
    }

    private String buildBlackPieceDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(buildIndexDescription(currentBoard, pieceDTO));
        description.append(pieceDTO.getCurrentCol());

        if (isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMove.ACROSS.getValue())
                    .append(toCol);

        } else {
            if (isUpMoving(false, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMove.UP.getValue());
            } else {
                description.append(EMove.DOWN.getValue());
            }

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(Math.abs(pieceDTO.getCurrentRow() - toRow));
            } else {
                description.append(toCol);
            }
        }

        return description.toString();
    }

    private String buildIndexDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO) {
        PieceDTO foundPiece = findExistingTheSameInColPath(currentBoard, pieceDTO);
        return foundPiece != null
                ? (pieceDTO.isRed() && (pieceDTO.getCurrentRow() < foundPiece.getCurrentRow()))
                        || (!pieceDTO.isRed() && (pieceDTO.getCurrentRow() > foundPiece.getCurrentRow()))
                                ? EIndex.BEFORE.getValue()
                                : EIndex.AFTER.getValue()
                : "";
    }

    private PieceDTO findExistingTheSameInColPath(PlayBoardDTO currentBoard, PieceDTO pieceDTO) {
        int colMoving = pieceDTO.getCurrentCol() - 1;
        for (int row = 0; row < MAX_ROW; row++) {
            PieceDTO currentPiece = currentBoard.getState()[colMoving][row];
            if (currentPiece != null
                    && currentPiece.getId() != pieceDTO.getId()
                    && currentPiece.isRed() == pieceDTO.isRed()
                    && currentPiece.getName().equals(pieceDTO.getName())) {
                return currentPiece;
            }
        }

        return null;
    }

    private boolean isHorizontalMoving(int fromRow, int toRow) {
        return fromRow == toRow;
    }

    private boolean isUpMoving(boolean isRed, int fromRow, int toRow) {
        return (isRed && fromRow > toRow) || (!isRed && fromRow < toRow);
    }

    private boolean isVerticallyMoving(int fromCol, int toCol) {
        return fromCol == toCol;
    }
}
