
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
import com.service.MoveRuleService;
import com.service.MoveTypeService;
import com.service.PieceService;
import com.service.PlayBoardService;

@Service
public class MoveRuleServiceImpl implements MoveRuleService {

    private static final int MIN_AREA = 0;
    private static final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private static final int MAX_ROW = Default.Game.PlayBoardSize.ROW;
    private static final List<Integer> PALACE_CENTER_COLS = IntStream.rangeClosed(3, 5)
            .boxed()
            .collect(Collectors.toList());
    private static final List<Integer> PALACE_CENTER_ROWS_FOR_BLACK = IntStream.rangeClosed(0, 2)
            .boxed()
            .collect(Collectors.toList());
    private static final List<Integer> PALACE_CENTER_ROWS_FOR_RED = IntStream.rangeClosed(7, 9)
            .boxed()
            .collect(Collectors.toList());

    private final PieceService pieceService;
    private final PlayBoardService playBoardDTOService;
    private final MoveTypeService moveTypeService;

    @Autowired
    public MoveRuleServiceImpl(
            PieceService pieceService,
            PlayBoardService playBoardDTOService,
            MoveTypeService moveTypeService) {
                
        this.pieceService = pieceService;
        this.playBoardDTOService = playBoardDTOService;
        this.moveTypeService = moveTypeService;
    }

    @Override
    public boolean isAvailableMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        boolean isValidMoveRule = isValidMove(playBoardDTO, pieceDTO, toCol, toRow);
        if (isValidMoveRule) {
            PlayBoardDTO playBoardDTOAfterMoving = playBoardDTOService.update(playBoardDTO, pieceDTO, toCol, toRow);
            // check general in safe after moving
            return isGeneralInSafe(playBoardDTOAfterMoving, pieceDTO);
        }

        return false;
    }

    @Override
    public boolean isValidMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        int fromCol = pieceDTO.getCurrentCol();
        int fromRow = pieceDTO.getCurrentRow();

        if (!isMoving(fromCol, fromRow, toCol, toRow) || !isValidArea(fromCol, toCol, fromRow, toRow)) {
            return false;
        }

        PieceDTO targetPiece = playBoardDTO.getState()[toCol][toRow];
        // check targetPiece is the same color with pieceDTO
        if (targetPiece != null && targetPiece.isRed() == pieceDTO.isRed()) {
            return false;
        }

        EPiece pieceType = pieceService.convertByName(pieceDTO.getName());
        switch (pieceType) {

            case GENERAL:
                return checkGeneralMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case GUARD:
                return checkGuardMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case ELEPHANT:
                return checkElephantMoveRule(playBoardDTO, pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            case HORSE:
                return checkHorseMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            case CHARIOT:
                return checkChariotMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            case CANNON:
                return checkCannonMoveRule(playBoardDTO, fromCol, toCol, fromRow, toRow);

            case SOLDIER:
                return checkSoldierMoveRule(pieceDTO.isRed(), fromCol, toCol, fromRow, toRow);

            default:
                return false;
        }
    }

    private boolean isGeneralInSafe(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        PieceDTO sameColorGeneral = null;
        PieceDTO opponentGeneral = null;

        List<PieceDTO> generalsPiece = pieceService.findAllInBoard(playBoardDTO, EPiece.GENERAL.name(), null);

        if (generalsPiece.get(0).isRed() == pieceDTO.isRed()) {
            sameColorGeneral = generalsPiece.get(0);
            opponentGeneral = generalsPiece.get(1);
        } else {
            sameColorGeneral = generalsPiece.get(1);
            opponentGeneral = generalsPiece.get(0);
        }

        if (sameColorGeneral != null && opponentGeneral != null) {
            return !areTwoGeneralsFacing(playBoardDTO, sameColorGeneral, opponentGeneral)
                    && !isGeneralBeingChecked(playBoardDTO, sameColorGeneral);
        }

        return false;
    }

    @Override
    public boolean isGeneralBeingChecked(PlayBoardDTO playBoardDTO, PieceDTO generalPiece) {
        List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoardDTO, null, !generalPiece.isRed());

        return opponentPiecesInBoard.stream()
                .anyMatch(opponentPiece -> isValidMove(
                        playBoardDTO, opponentPiece, generalPiece.getCurrentCol(), generalPiece.getCurrentRow()));
    }

    private boolean areTwoGeneralsFacing(PlayBoardDTO playBoardDTO, PieceDTO generalPiece1, PieceDTO generalPiece2) {
        if (generalPiece1.getCurrentCol() == generalPiece2.getCurrentCol()) {
            int currentCol = generalPiece1.getCurrentCol();
            int fromRow = generalPiece1.getCurrentRow();
            int toRow = generalPiece2.getCurrentRow();

            if (!pieceService.existsBetweenInColPath(playBoardDTO, currentCol, fromRow, toRow)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMoving(int fromCol, int fromRow, int toCol, int toRow) {
        return fromCol != toCol || fromRow != toRow;
    }

    private boolean isValidArea(int fromCol, int toCol, int fromRow, int toRow) {
        return fromCol >= MIN_AREA && fromCol <= MAX_COL && toCol >= MIN_AREA && toCol <= MAX_COL
                && fromRow >= MIN_AREA && fromRow <= MAX_ROW && toRow >= MIN_AREA && toRow <= MAX_ROW;
    }

    /*
     * GENERALS can move only one space horizontally or vertically in a move
     * and must always stay within the palace
     */
    private boolean checkGeneralMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
        return isInAreaCenter(isRed, toCol, toRow)
                && ((moveTypeService.isHorizontalMoving(fromRow, toRow) && Math.abs(toCol - fromCol) == 1)
                        || (moveTypeService.isVerticallyMoving(fromCol, toCol) && Math.abs(toRow - fromRow) == 1));
    }

    /*
     * ADVISORS can move only one space at a time diagonally in a move
     * and must stay within the palace.
     */
    private boolean checkGuardMoveRule(boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {
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
            PlayBoardDTO playBoardDTO, boolean isRed, int fromCol, int toCol, int fromRow, int toRow) {

        int verticalSpace = Math.abs(toRow - fromRow);
        int horizontalSpace = Math.abs(toCol - fromCol);

        boolean isDiagonalMove = ((verticalSpace == 2) && (horizontalSpace == 2));

        boolean isWithinOwnHalf = !isOutOfOwnHalf(isRed, toRow);

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

        if (verticalSpace == 2 && horizontalSpace == 1) {
            int obstacleCol = fromCol;
            int obstacleRow = (fromRow + toRow) / 2;

            return playBoardDTO.getState()[obstacleCol][obstacleRow] == null; // No obstacle, Invalid move
        } else if (verticalSpace == 1 && horizontalSpace == 2) {
            int obstacleCol = (fromCol + toCol) / 2;
            int obstacleRow = fromRow;

            return playBoardDTO.getState()[obstacleCol][obstacleRow] == null; // No obstacle, Invalid move
        }

        return false; // Invalid move
    }

    /*
     * CHARIOTS can move one or more spaces vertically or horizontally in a move
     * without any pieces between
     */
    private boolean checkChariotMoveRule(PlayBoardDTO playBoardDTO, int fromCol, int toCol, int fromRow, int toRow) {
        return (moveTypeService.isVerticallyMoving(fromCol, toCol)
                && !pieceService.existsBetweenInColPath(playBoardDTO, fromCol, fromRow, toRow))
                || (moveTypeService.isHorizontalMoving(fromRow, toRow)
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
        boolean isHorizontalMoving = moveTypeService.isHorizontalMoving(fromRow, toRow);
        // check move Vertically or Horizontal
        if (isVerticallyMoving || isHorizontalMoving) {
            int numPiecesBetween = isVerticallyMoving
                    ? pieceService.countBetweenInColPath(playBoardDTO, fromCol, fromRow, toRow)
                    : pieceService.countBetweenInRowPath(playBoardDTO, fromRow, fromCol, toCol);

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
        return (moveTypeService.isVerticallyMoving(fromCol, toCol)
                && (isRed ? (fromRow - toRow == 1) : (toRow - fromRow == 1)))
                || (moveTypeService.isHorizontalMoving(fromRow, toRow)
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

}
