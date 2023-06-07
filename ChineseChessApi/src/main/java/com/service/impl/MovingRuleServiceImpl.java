package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.exception.ResourceNotFoundException;
import com.service.MovingRuleService;
import com.service.PieceService;

@Service
public class MovingRuleServiceImpl implements MovingRuleService {
    private static final int MIN_AREA = 0;
    private static final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private static final int MAX_ROW = Default.Game.PlayBoardSize.ROW;
    private static final List<Integer> PALACE_CENTER_COLS = IntStream.rangeClosed(4, 6).boxed()
            .collect(Collectors.toList());
    private static final List<Integer> PALACE_CENTER_ROWS_FOR_BLACK = IntStream.rangeClosed(1, 3).boxed()
            .collect(Collectors.toList());
    private static final List<Integer> PALACE_CENTER_ROWS_FOR_RED = IntStream.rangeClosed(8, 10).boxed()
            .collect(Collectors.toList());

    @Autowired
    private PieceService pieceService;

    @Override
    public boolean isMoveValid(PlayBoardDTO currentBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        int fromCol = pieceDTO.getCurrentCol();
        int fromRow = pieceDTO.getCurrentRow();
        if (!isValidArea(fromCol, toCol, fromRow, toRow)) {
            return false;
        } else {
            PieceDTO targetPiece = currentBoard.getState()[toCol - 1][toRow - 1];
            if (isSameColorPiece(pieceDTO, targetPiece)) {
                return false;
            }

            EPiece pieceType = pieceService.convertByName(pieceDTO.getName());
            switch (pieceType) {
                case General:
                    return isGeneralMoveValid(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

                case Advisor:
                    return isAdvisorMoveValid(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

                case Elephant:
                    return isElephantMoveValid(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

                case Horse:
                    return isHorseMoveValid(fromCol, toCol, fromRow, toRow, currentBoard.getState());

                case Chariot:
                    return isChariotMoveValid(fromCol, toCol, fromRow, toRow, currentBoard.getState());

                case Cannon:
                    return isCannonMoveValid(fromCol, toCol, fromRow, toRow, currentBoard.getState(), targetPiece);

                case Soldier:
                    return isSoldierMoveValid(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow, targetPiece);

                default:
                    throw new ResourceNotFoundException(Collections.singletonMap("Piece", pieceType.name()));
            }
        }
    }

    private boolean isValidArea(int fromCol, int toCol, int fromRow, int toRow) {
        return fromCol >= MIN_AREA && fromCol <= MAX_COL && toCol >= MIN_AREA && toCol <= MAX_COL
                && fromRow >= MIN_AREA && fromRow <= MAX_ROW && toRow >= MIN_AREA && toRow <= MAX_ROW;
    }

    private boolean isSameColorPiece(PieceDTO pieceDTO, PieceDTO targetPiece) {
        return targetPiece != null && pieceDTO.isRed() == targetPiece.isRed();
    }

    private boolean isGeneralMoveValid(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return isAreaCenter(isRed, toCol, toRow)
                && ((isHorizontalMoving(fromRow, toRow) && Math.abs(toCol - fromCol) == 1)
                        || (isVerticallyMoving(fromCol, toCol) && Math.abs(toRow - fromRow) == 1));
    }

    private boolean isAdvisorMoveValid(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return isAreaCenter(isRed, toCol, toRow) && Math.abs(fromCol - toCol) == 1 && Math.abs(fromRow - toRow) == 1;
    }

    private boolean isElephantMoveValid(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return rowDiff == 2 && colDiff == 2 && !isOverTheRiver(isRed, toRow);
    }

    private boolean isHorseMoveValid(int fromCol, int toCol, int fromRow, int toRow, PieceDTO[][] state) {
        int rowOffset = Math.abs(toRow - fromRow);
        int colOffset = Math.abs(toCol - fromCol);
        return (rowOffset == 2 && colOffset == 1 && state[fromCol][(fromRow + toRow) / 2] == null)
                || (rowOffset == 1 && colOffset == 2 && state[(fromCol + toCol) / 2][fromRow] == null);
    }

    private boolean isChariotMoveValid(int fromCol, int toCol, int fromRow, int toRow, PieceDTO[][] state) {
        if (isVerticallyMoving(fromCol, toCol)) {
            return !existingPieceBetween(state, fromRow, toRow, fromCol);
        } else if (isHorizontalMoving(fromRow, toRow)) {
            return !existingPieceBetween(state, fromCol, toCol, fromRow);
        }
        return false;
    }

    private boolean isCannonMoveValid(int fromCol, int toCol, int fromRow, int toRow, PieceDTO[][] state,
            PieceDTO targetPiece) {
        if (isVerticallyMoving(fromCol, toCol) || isHorizontalMoving(fromRow, toRow)) {
            int numPiecesBetween;
            if (isVerticallyMoving(fromCol, toCol)) {
                numPiecesBetween = countPiecesBetween(state, fromCol, toCol, fromRow);
            } else {
                numPiecesBetween = countPiecesBetween(state, fromRow, toRow, fromCol);
            }

            return (targetPiece == null && numPiecesBetween == 0) // Moving to an empty space
                    || (targetPiece != null && numPiecesBetween == 1); // Jump
        }
        return false;
    }

    private boolean isSoldierMoveValid(boolean isRed, int fromCol, int toCol, int fromRow, int toRow,
            PieceDTO targetPiece) {
        if (targetPiece == null || isRed != targetPiece.isRed()) {
            int forwardMoveStep = isRed ? (fromRow - toRow) : (toRow - fromRow);
            int horizontalMoveStep = Math.abs(fromCol - toCol);

            if (isVerticallyMoving(fromCol, toCol) && forwardMoveStep == 1) {
                return true; // Valid forward move
            } else if (isOverTheRiver(isRed, fromRow) && isHorizontalMoving(fromRow, toRow)
                    && horizontalMoveStep == 1) {
                return true; // Valid horizontal move after crossing the river
            }
        }
        return false;
    }

    private boolean isAreaCenter(boolean isRed, int col, int row) {
        List<Integer> centerRows = isRed ? PALACE_CENTER_ROWS_FOR_RED : PALACE_CENTER_ROWS_FOR_BLACK;
        return PALACE_CENTER_COLS.contains(col) && centerRows.contains(row);
    }

    private boolean isOverTheRiver(boolean isRed, int row) {
        return (isRed && PALACE_CENTER_ROWS_FOR_BLACK.contains(row))
                || (!isRed && PALACE_CENTER_ROWS_FOR_RED.contains(row));
    }

    private boolean isHorizontalMoving(int fromRow, int toRow) {
        return fromRow == toRow;
    }

    private boolean isVerticallyMoving(int fromCol, int toCol) {
        return fromCol == toCol;
    }

    private boolean existingPieceBetween(PieceDTO[][] state, int fixedCoord,
            int movingCoordStart, int movingCoordEnd) {
        int start = Math.min(movingCoordStart, movingCoordEnd);
        int end = Math.max(movingCoordStart, movingCoordEnd);
        for (int i = start; i <= end; i++) {
            if (state[fixedCoord][i] != null) {
                return true;
            }
        }
        return false;
    }

    private int countPiecesBetween(PieceDTO[][] state, int fixedCoord, int movingCoordStart, int movingCoordEnd) {
        int start = Math.min(movingCoordStart, movingCoordEnd);
        int end = Math.max(movingCoordStart, movingCoordEnd);
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (state[fixedCoord][i] != null) {
                count++;
            }
        }
        return count;
    }
}
