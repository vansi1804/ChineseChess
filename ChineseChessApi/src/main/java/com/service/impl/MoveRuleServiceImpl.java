package com.service.impl;

import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveRuleService;
import com.service.MoveTypeService;
import com.service.PieceService;

@Service
public class MoveRuleServiceImpl implements MoveRuleService {

    private static final int CENTER_COL_INDEX_MIN = Default.Game.PlayBoardSize.CENTER_COL_MIN - 1;
    private static final int CENTER_COL_INDEX_MAX = Default.Game.PlayBoardSize.CENTER_COL_MAX - 1;

    private static final int BLACK_ROW_INDEX_MAX = Default.Game.PlayBoardSize.BlackArea.ROW_MAX - 1;
    private static final int BLACK_CENTER_ROW_INDEX_MIN = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MIN - 1;
    private static final int BLACK_CENTER_ROW_INDEX_MAX = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MAX - 1;

    private static final int RED_ROW_INDEX_MIN = Default.Game.PlayBoardSize.RedArea.ROW_MIN - 1;
    private static final int RED_CENTER_ROW_INDEX_MIN = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MIN - 1;
    private static final int RED_CENTER_ROW_INDEX_MAX = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MAX - 1;

    private final PieceService pieceService;
    private final MoveTypeService moveTypeService;

    public MoveRuleServiceImpl(
            PieceService pieceService,
            MoveTypeService moveTypeService) {

        this.pieceService = pieceService;
        this.moveTypeService = moveTypeService;
    }

    @Override
    public boolean isValid(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        int fromCol = pieceDTO.getCurrentCol();
        int fromRow = pieceDTO.getCurrentRow();

        // check piece is not moving out current index
        if ((fromCol == toCol) && (fromRow == toRow)) {
            return false;
        }

        PieceDTO targetPiece = playBoardDTO.getState()[toCol][toRow];
        // check targetPiece is not the same color with pieceDTO
        if ((targetPiece != null) && (targetPiece.isRed() == pieceDTO.isRed())) {
            return false;
        }

        EPiece ePiece = pieceService.convertByName(pieceDTO.getName());
        switch (ePiece) {
            case SOLDIER:
                return checkSoldierMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case CANNON:
                return checkCannonMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            case GUARD:
                return checkGuardMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case ELEPHANT:
                return checkElephantMoveRule(playBoardDTO, pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case HORSE:
                return checkHorseMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            case CHARIOT:
                return checkChariotMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            default: // case GENERAL:
                return checkGeneralMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);
        }
    }

    /*
     * GENERALS can move only one space horizontally or vertically in a move
     * and must always stay within the palace
     */
    private boolean checkGeneralMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return isInAreaCenter(isRed, toCol, toRow)
                && ((moveTypeService.isHorizontallyMoving(fromRow, toRow) && (Math.abs(toCol - fromCol) == 1))
                        || (moveTypeService.isVerticallyMoving(fromCol, toCol) && (Math.abs(toRow - fromRow) == 1)));
    }

    /*
     * ADVISORS can move only one space at a time diagonally in a move
     * and must stay within the palace.
     */
    private boolean checkGuardMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);
        boolean isDiagonalMove = ((verticalSpace == 1) && (horizontalSpace == 1));

        return isDiagonalMove && isInAreaCenter(isRed, toCol, toRow);
    }

    /*
     * ELEPHANTS can move two spaces at a time diagonally in a move
     * (i.e. 2 spaces vertically and 2 spaces horizontally).
     * They must stay within their own half.
     * If there is an obstacle(a piece midway) between the original and final
     * intended position, the elephant is blocked and not allowed.
     */
    private boolean checkElephantMoveRule(
            PlayBoardDTO playBoardDTO, boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {

        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);

        boolean isDiagonalMove = ((verticalSpace == 2) && (horizontalSpace == 2));

        boolean isWithinOwnHalf = !isOverTheRiver(isRed, toRow);

        int middleCol = (fromCol + toCol) / 2;
        int middleRow = (fromRow + toRow) / 2;
        boolean existingObstacle = playBoardDTO.getState()[middleCol][middleRow] != null;

        return isDiagonalMove && isWithinOwnHalf && !existingObstacle;
    }

    /*
     * A HORSES can move with two spaces horizontally and one space vertically
     * (or respectively 2 spaces vertically and one space horizontally) in a move.
     * If there is an obstacle (a piece next to the horse in the horizontal
     * or vertical direction), the horse is blocked and the move is not allowed.
     */
    private boolean checkHorseMoveRule(PlayBoardDTO playBoardDTO, int fromCol, int toCol, int fromRow, int toRow) {
        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);

        // check no obstacle
        if ((verticalSpace == 2) && (horizontalSpace == 1)) {
            int obstacleCol = fromCol;
            int obstacleRow = (fromRow + toRow) / 2;

            return playBoardDTO.getState()[obstacleCol][obstacleRow] == null;
        } else if ((verticalSpace == 1) && (horizontalSpace == 2)) {
            int obstacleCol = (fromCol + toCol) / 2;
            int obstacleRow = fromRow;

            return playBoardDTO.getState()[obstacleCol][obstacleRow] == null;
        } else {
            return false;
        }
    }

    /*
     * CHARIOTS can move one or more spaces vertically or horizontally in a move *
     * without any pieces between
     */
    private boolean checkChariotMoveRule(PlayBoardDTO playBoardDTO, int fromCol, int toCol, int fromRow, int toRow) {
        return (moveTypeService.isVerticallyMoving(fromCol, toCol)
                && !pieceService.existsBetweenInColPath(playBoardDTO, fromCol, fromRow, toRow))
                || (moveTypeService.isHorizontallyMoving(fromRow, toRow)
                        && !pieceService.existsBetweenInRowPath(playBoardDTO, fromRow, fromCol, toCol));
    }

    /*
     * CANNONS can move one or more spaces vertically or horizontally in a move.
     * If moving to an empty position, there must not be existing any pieces between
     * If moving to a non-empty position, there must be exactly 1 between
     */
    private boolean checkCannonMoveRule(PlayBoardDTO playBoardDTO, int fromCol, int toCol, int fromRow, int toRow) {
        PieceDTO targetPiece = playBoardDTO.getState()[toCol][toRow];

        boolean isVerticallyMoving = moveTypeService.isVerticallyMoving(fromCol, toCol);
        boolean isHorizontalMoving = moveTypeService.isHorizontallyMoving(fromRow, toRow);

        // check move Vertically or Horizontal
        if (isVerticallyMoving || isHorizontalMoving) {
            int numPiecesBetween = isVerticallyMoving
                    ? pieceService.countBetweenInColPath(playBoardDTO, fromCol, fromRow, toRow)
                    : pieceService.countBetweenInRowPath(playBoardDTO, fromRow, fromCol, toCol);

            // check position moving to and number of piece between in two case
            return targetPiece == null ? numPiecesBetween == 0 : numPiecesBetween == 1;
        } else {
            return false;
        }
    }

    /*
     * SOLDIERS can move with only one space in a move.
     * If being in own half, they can move forward vertically only.
     * If being out own half(over the river yet), they can move horizontally also.
     */
    private boolean checkSoldierMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return (moveTypeService.isVerticallyMoving(fromCol, toCol)
                && (isRed ? (fromRow - toRow == 1) : (toRow - fromRow == 1)))
                || (moveTypeService.isHorizontallyMoving(fromRow, toRow)
                        && isOverTheRiver(isRed, fromRow) && (Math.abs(fromCol - toCol) == 1));
    }

    private boolean isInAreaCenter(boolean isRed, int col, int row) {
        return ((CENTER_COL_INDEX_MIN <= col) && (col <= CENTER_COL_INDEX_MAX))
                && (isRed ? ((RED_CENTER_ROW_INDEX_MIN <= row) && (row <= RED_CENTER_ROW_INDEX_MAX))
                        : ((BLACK_CENTER_ROW_INDEX_MIN <= row) && (row <= BLACK_CENTER_ROW_INDEX_MAX)));
    }

    private boolean isOverTheRiver(boolean isRed, int row) {
        return isRed ? (row < RED_ROW_INDEX_MIN) : (row > BLACK_ROW_INDEX_MAX);
    }

}
