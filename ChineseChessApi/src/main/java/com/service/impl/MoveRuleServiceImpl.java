
package com.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MovingRuleService;
import com.service.PieceService;
import com.service.PlayBoardService;

@Service
public class MoveRuleServiceImpl implements MovingRuleService {
    private static final int MIN_AREA = 1;
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
    @Autowired
    private PlayBoardService playBoardService;

    @Override
    public boolean isMoveValid(PlayBoardDTO playBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        boolean isValidMoveRule = checkMoveRule(playBoard, pieceDTO, toCol, toRow);
        if (isValidMoveRule) {
            PlayBoardDTO playBoardAfterMoving = playBoardService.update(playBoard, pieceDTO, toCol, toRow);
            // check general in safe after moving
            return isGeneralInSafe(playBoardAfterMoving, pieceDTO);
        }

        return false;
    }

    private boolean isGeneralInSafe(PlayBoardDTO playBoard, PieceDTO pieceDTO) {

        PieceDTO generalSameColor = null;
        PieceDTO opponentGeneral = null;

        List<PieceDTO> generalsPiece = pieceService.findAllInBoard(playBoard, EPiece.General.getFullNameValue(), null);

        if (isSameColor(generalsPiece.get(0).isRed(), pieceDTO.isRed())) {
            generalSameColor = generalsPiece.get(0);
            opponentGeneral = generalsPiece.get(1);
        } else {
            generalSameColor = generalsPiece.get(1);
            opponentGeneral = generalsPiece.get(0);
        }

        if (generalSameColor != null && opponentGeneral != null) {
            return !areTwoGeneralsFacing(playBoard, generalSameColor, opponentGeneral)
                    && !isGeneralBeingChecked(playBoard, generalSameColor);
        }
        return false;
    }

    @Override
    public boolean areTwoGeneralsFacing(PlayBoardDTO playBoard, PieceDTO generalPiece1, PieceDTO generalPiece2) {
        if (generalPiece1.getCurrentCol() == generalPiece2.getCurrentCol()) {
            if (existsPieceBetweenInColPath(playBoard.getState(), generalPiece1.getCurrentCol(),
                    generalPiece1.getCurrentRow(), generalPiece2.getCurrentRow())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGeneralBeingChecked(PlayBoardDTO playBoard, PieceDTO generalPiece) {
        if (generalPiece == null) {
            return false;
        }

        List<PieceDTO> opponentInBoard = pieceService.findAllInBoard(playBoard, null, !generalPiece.isRed());

        int toCol = generalPiece.getCurrentCol();
        int toRow = generalPiece.getCurrentRow();
        for (PieceDTO pieceInBoard : opponentInBoard) {
            boolean existsOpponentThreadGeneral = checkMoveRule(playBoard, pieceInBoard, toCol, toRow);
            if (existsOpponentThreadGeneral) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMoveRule(PlayBoardDTO playBoard, PieceDTO pieceDTO, int toCol, int toRow) {
        int fromCol = pieceDTO.getCurrentCol();
        int fromRow = pieceDTO.getCurrentRow();

        if (!isMoving(fromCol, fromRow, toCol, toRow) || !isValidArea(fromCol, toCol, fromRow, toRow)) {
            return false;
        }

        PieceDTO targetPiece = playBoard.getState()[toCol - 1][toRow - 1];
        if (targetPiece != null && isSameColor(pieceDTO.isRed(), targetPiece.isRed())) {
            return false;
        }

        EPiece pieceType = pieceService.convertByName(pieceDTO.getName());
        switch (pieceType) {

            case General:
                return checkGeneralMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case Advisor:
                return checkAdvisorMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case Elephant:
                return checkElephantMoveRule(playBoard.getState(), pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case Horse:
                return checkHorseMoveRule(playBoard.getState(), fromCol, toCol, fromRow, toRow);

            case Chariot:
                return checkChariotMoveRule(playBoard.getState(), fromCol, toCol, fromRow, toRow);

            case Cannon:
                return checkCannonMoveRule(playBoard.getState(), fromCol, toCol, fromRow, toRow);

            case Soldier:
                return checkSoldierMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            default:
                return false;
        }
    }

    private boolean isMoving(int fromCol, int fromRow, int toCol, int toRow) {
        return fromCol != toCol || fromRow != toRow;
    }

    private boolean isValidArea(int fromCol, int toCol, int fromRow, int toRow) {
        return fromCol >= MIN_AREA && fromCol <= MAX_COL && toCol >= MIN_AREA && toCol <= MAX_COL
                && fromRow >= MIN_AREA && fromRow <= MAX_ROW && toRow >= MIN_AREA && toRow <= MAX_ROW;
    }

    private boolean isSameColor(boolean color1, boolean color2) {
        return color1 == color2;
    }

    /*
     * GENERALS can move only one space horizontally or vertically in a move
     * and must always stay within the palace
     */
    private boolean checkGeneralMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {

        return isInAreaCenter(isRed, toCol, toRow)
                && ((isHorizontalMoving(fromRow, toRow) && Math.abs(toCol - fromCol) == 1)
                        || (isVerticallyMoving(fromCol, toCol) && Math.abs(toRow - fromRow) == 1));
    }

    /*
     * ADVISORS can move only one space at a time diagonally in a move
     * and must stay within the palace.
     */
    private boolean checkAdvisorMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);
        boolean isDiagonalMove = ((verticalSpace == 1) && (horizontalSpace == 1));

        return isInAreaCenter(isRed, toCol, toRow) && isDiagonalMove;
    }

    /*
     * ELEPHANTS can move two spaces at a time diagonally in a move
     * (i.e. 2 spaces vertically and 2 spaces horizontally).
     * They must stay within their own half.
     * If there is an obstacle(a piece midway) between the original and final
     * intended position, the elephant is blocked and not allowed.
     */
    private boolean checkElephantMoveRule(
            PieceDTO[][] state, boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {

        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);

        boolean isDiagonalMove = ((verticalSpace == 2) && (horizontalSpace == 2));

        boolean isWithinOwnHalf = !isOutOfOwnHalf(isRed, toRow);

        int middleCol = (fromCol + toCol) / 2;
        int middleRow = (fromRow + toRow) / 2;
        boolean existingObstacle = state[middleCol - 1][middleRow - 1] != null;

        return isDiagonalMove && isWithinOwnHalf && !existingObstacle;
    }

    /*
     * A HORSES can move with two spaces horizontally and one space vertically
     * (or respectively 2 spaces vertically and one space horizontally) in a move.
     * If there is an obstacle (a piece next to the horse in the horizontal
     * or vertical direction), the horse is blocked and the move is not allowed.
     */
    private boolean checkHorseMoveRule(PieceDTO[][] state, int fromCol, int toCol, int fromRow, int toRow) {

        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);

        if (verticalSpace == 2 && horizontalSpace == 1) {
            int obstacleCol = fromCol;
            int obstacleRow = (fromRow + toRow) / 2;

            return state[obstacleCol - 1][obstacleRow - 1] == null; // No obstacle, Invalid move
        } else if (verticalSpace == 1 && horizontalSpace == 2) {
            int obstacleCol = (fromCol + toCol) / 2;
            int obstacleRow = fromRow;

            return state[obstacleCol - 1][obstacleRow - 1] == null; // No obstacle, Invalid move
        }

        return false; // Invalid move
    }

    /*
     * CHARIOTS can move one or more spaces vertically or horizontally in a move
     * without any pieces between
     */
    private boolean checkChariotMoveRule(PieceDTO[][] state, int fromCol, int toCol, int fromRow, int toRow) {

        return (isVerticallyMoving(fromCol, toCol) && !existsPieceBetweenInColPath(state, fromCol, fromRow, toRow))
                || (isHorizontalMoving(fromRow, toRow) && !existsPieceBetweenInRowPath(state, fromRow, fromCol, toCol));
    }

    /*
     * CANNONS can move one or more spaces vertically or horizontally in a move.
     * If moving to an empty position, there must not be existing any pieces between
     * If moving to a non-empty position, there must be exactly 1 between
     */
    private boolean checkCannonMoveRule(PieceDTO[][] state, int fromCol, int toCol, int fromRow, int toRow) {
        PieceDTO targetPiece = state[toCol - 1][toRow - 1];

        boolean isVerticallyMoving = isVerticallyMoving(fromCol, toCol);
        boolean isHorizontalMoving = isHorizontalMoving(fromRow, toRow);
        // check move Vertically or Horizontal
        if (isVerticallyMoving || isHorizontalMoving) {
            int numPiecesBetween = isVerticallyMoving
                    ? countPiecesBetweenInColPath(state, fromCol, fromRow, toRow)
                    : countPiecesBetweenInRowPath(state, fromRow, fromCol, toCol);

            // check position moving to and number of piece between in two case
            return targetPiece == null ? numPiecesBetween == 0 : numPiecesBetween == 1;
        }
        return false;
    }

    /*
     * SOLDIERS can move with only one space in a move.
     * If being in own half, they can move forward vertically only.
     * If being out own half(over the river yet), they can move horizontally also.
     */
    private boolean checkSoldierMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return (isVerticallyMoving(fromCol, toCol) && (isRed ? (fromRow - toRow == 1) : (toRow - fromRow == 1)))
                || (isHorizontalMoving(fromRow, toRow)
                        && isOutOfOwnHalf(isRed, fromRow) && (Math.abs(fromCol - toCol) == 1));
    }

    private boolean isInAreaCenter(boolean isRed, int col, int row) {
        List<Integer> centerCols = PALACE_CENTER_COLS;
        List<Integer> centerRows = isRed ? PALACE_CENTER_ROWS_FOR_RED : PALACE_CENTER_ROWS_FOR_BLACK;

        return centerCols.contains(col) && centerRows.contains(row);
    }

    private boolean isOutOfOwnHalf(boolean isRed, int row) {
        List<Integer> halfRow = isRed ? PALACE_CENTER_ROWS_FOR_BLACK : PALACE_CENTER_ROWS_FOR_RED;

        return halfRow.contains(row);
    }

    private boolean isHorizontalMoving(int fromRow, int toRow) {
        return fromRow == toRow;
    }

    private boolean isVerticallyMoving(int fromCol, int toCol) {
        return fromCol == toCol;
    }

    private boolean existsPieceBetweenInRowPath(PieceDTO[][] state, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return IntStream.rangeClosed(startCol, endCol)
                .anyMatch(col -> state[col - 1][currentRow - 1] != null);
    }

    private boolean existsPieceBetweenInColPath(PieceDTO[][] state, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return IntStream.rangeClosed(startRow, endRow)
                .anyMatch(row -> state[currentCol - 1][row - 1] != null);
    }

    private int countPiecesBetweenInRowPath(PieceDTO[][] state, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return (int) IntStream.rangeClosed(startCol, endCol)
                .filter(col -> state[col - 1][currentRow - 1] != null)
                .count();
    }

    private int countPiecesBetweenInColPath(PieceDTO[][] state, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return (int) IntStream.rangeClosed(startRow, endRow)
                .filter(row -> state[currentCol - 1][row - 1] != null)
                .count();
    }

}
