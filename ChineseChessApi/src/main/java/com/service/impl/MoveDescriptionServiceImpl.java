package com.service.impl;

import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EIndex;
import com.common.enumeration.EMove;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.service.MoveDescriptionService;

@Service
public class MoveDescriptionServiceImpl implements MoveDescriptionService {

    @Override
    public String getDescription(PlayBoardDTO currentBoard, MoveHistory moveHistory) {
        Object pieceName = moveHistory.getPiece().getName().charAt(0);
        Object index = "";
        Object from;
        Object move;
        Object to;
        // check move across
        if (isMovingAcross(moveHistory.getFromRow(), moveHistory.getToRow())) {
            move = EMove.ACROSS.getValue();
            if (moveHistory.getPiece().isRed()) {
                from = Default.PLAY_BOARD_COL_SIZE - moveHistory.getFromCol() + 1;
                to = Default.PLAY_BOARD_COL_SIZE - moveHistory.getToCol() + 1;
            } else {
                from = moveHistory.getFromCol();
                to = moveHistory.getToCol();
            }
        } else { // check move up/down
            // for red
            if (moveHistory.getPiece().isRed()) {
                from = moveHistory.getFromCol();
                // check move up
                if (isMovingUp(true, moveHistory.getFromRow(), moveHistory.getToRow())) {
                    move = EMove.UP.getValue();
                    // check move vertical
                    to = isMovingVertical(moveHistory.getFromCol(), moveHistory.getToCol())
                            ? (moveHistory.getFromRow() - moveHistory.getToRow() + 1)
                            : Default.PLAY_BOARD_COL_SIZE - moveHistory.getToCol() + 1;
                } else { // check move down
                    move = EMove.DOWN.getValue();
                    // check move vertical
                    to = isMovingVertical(moveHistory.getFromCol(), moveHistory.getToCol())
                            ? (moveHistory.getToRow() - moveHistory.getFromRow() + 1)
                            : Default.PLAY_BOARD_COL_SIZE - moveHistory.getToCol() + 1;
                }
                PieceDTO anotherTheSamePieceInColMoving = existingAnotherTheSamePieceInColMoving(
                        currentBoard, moveHistory);
                if (anotherTheSamePieceInColMoving != null) {
                    // check moving red piece before or after another the same piece
                    index = (anotherTheSamePieceInColMoving.getCurrentCol() > moveHistory.getFromCol())
                            ? EIndex.BEFORE.getValue()
                            : EIndex.AFTER.getValue();
                }
            } else { // for black
                from = moveHistory.getFromCol();
                // check move up
                if (isMovingUp(false, moveHistory.getFromRow(), moveHistory.getToRow())) {
                    move = EMove.UP.getValue();
                    // check move vertical
                    to = isMovingVertical(moveHistory.getFromCol(), moveHistory.getToCol())
                            ? (moveHistory.getToRow() - moveHistory.getFromRow() + 1)
                            : moveHistory.getToCol();
                } else { // check move down
                    move = EMove.DOWN.getValue();
                    // check move vertical
                    to = isMovingVertical(moveHistory.getFromCol(), moveHistory.getToCol())
                            ? (moveHistory.getFromRow() - moveHistory.getToRow() + 1)
                            : moveHistory.getToCol();
                }
                PieceDTO anotherTheSamePieceInColMoving = existingAnotherTheSamePieceInColMoving(
                        currentBoard, moveHistory);
                if (anotherTheSamePieceInColMoving != null) {
                    // check moving red piece before or after another the same piece
                    index = (anotherTheSamePieceInColMoving.getCurrentCol() > moveHistory.getFromCol())
                            ? EIndex.AFTER.getValue()
                            : EIndex.BEFORE.getValue();
                }
            }
        }
        return pieceName.toString() + index.toString() + from.toString() + move.toString() + to.toString();
    }

    private PieceDTO existingAnotherTheSamePieceInColMoving(PlayBoardDTO currentBoard, MoveHistory moveHistory) {
        int colMoving = moveHistory.getFromCol() - 1;
        for (int row = 0; row < Default.PLAY_BOARD_RAW_SIZE; row++) {
            if (currentBoard.getState()[colMoving][row] != null
                    && currentBoard.getState()[colMoving][row].getId() != moveHistory.getPiece().getId()
                    && Boolean.compare(currentBoard.getState()[colMoving][row].isRed(),
                            moveHistory.getPiece().isRed()) == 0
                    && currentBoard.getState()[colMoving][row].getName().equals(moveHistory.getPiece().getName())) {
                return currentBoard.getState()[colMoving][row];
            }
        }
        return null;
    }

    private boolean isMovingAcross(int fromRow, int toRow) {
        return fromRow == toRow;
    }

    private boolean isMovingUp(boolean isRed, int fromRow, int toRow) {
        return (isRed && (fromRow > toRow)) || ((!isRed && (fromRow < toRow)));
    }

    private boolean isMovingVertical(int fromCol, int toCol) {
        return fromCol == toCol;
    }

}
