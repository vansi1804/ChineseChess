package com.service.impl;

import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EIndex;
import com.common.enumeration.EMove;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveDescriptionService;

@Service
public class MoveDescriptionServiceImpl implements MoveDescriptionService {
    private final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW;

    @Override
    public String buildDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        char pieceName = pieceDTO.getName().charAt(0);

        StringBuilder description = new StringBuilder();
        description.append(pieceName);

        if (pieceDTO.isRed()) {
            description.append(buildRedPieceDescription(currentBoard, pieceDTO, toCol, toRow));
        } else {
            description.append(buildBlackPieceDescription(currentBoard, pieceDTO, toCol, toRow));
        }

        return description.toString();
    }

    private String buildRedPieceDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(getIndexDescription(currentBoard, pieceDTO));

        if (isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.ACROSS.getValue())
                    .append(toCol);

        } else if (isUpMoving(true, pieceDTO.getCurrentRow(), toRow)) {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.UP.getValue());

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(pieceDTO.getCurrentRow() - toRow);
            } else {
                description.append(MAX_COL - toCol + 1);
            }
        } else {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.DOWN.getValue());

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(toRow - pieceDTO.getCurrentRow());
            } else {
                description.append(MAX_COL - toCol);
            }
        }

        return description.toString();
    }

    private String buildBlackPieceDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(getIndexDescription(currentBoard, pieceDTO));

        if (isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.ACROSS.getValue())
                    .append(toCol);

        } else if (isUpMoving(false, pieceDTO.getCurrentRow(), toRow)) {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.UP.getValue());

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(toRow - pieceDTO.getCurrentRow());
            } else {
                description.append(toCol);
            }
        } else {
            description.append(pieceDTO.getCurrentCol())
                    .append(EMove.DOWN.getValue());

            if (isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(pieceDTO.getCurrentRow() - toRow);
            } else {
                description.append(toCol);
            }
        }

        return description.toString();
    }

    private String getIndexDescription(PlayBoardDTO currentBoard, PieceDTO pieceDTO) {
        PieceDTO foundPiece = findExistingTheSameInColMoving(currentBoard, pieceDTO);
        return foundPiece != null
                ? (pieceDTO.isRed() && (pieceDTO.getCurrentRow() < foundPiece.getCurrentRow()))
                        || (!pieceDTO.isRed() && (pieceDTO.getCurrentRow() > foundPiece.getCurrentRow()))
                                ? EIndex.BEFORE.getValue()
                                : EIndex.AFTER.getValue()
                : "";
    }

    private PieceDTO findExistingTheSameInColMoving(PlayBoardDTO currentBoard, PieceDTO pieceDTO) {
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
