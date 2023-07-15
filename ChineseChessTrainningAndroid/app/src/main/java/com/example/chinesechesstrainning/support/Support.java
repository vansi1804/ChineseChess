package com.example.chinesechesstrainning.support;

import com.example.chinesechesstrainning.model.Piece;
import com.example.chinesechesstrainning.model.PlayBoard;

import java.util.Arrays;

public class Support {

    public static Piece findPieceInBoard(int pieceId, PlayBoard playBoard) {
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 10; row++) {
                if (playBoard.getState()[col][row] != null && playBoard.getState()[col][row].getId() == pieceId) {
                    return playBoard.getState()[col][row];
                }
            }
        }
        return null;
    }

    public static PlayBoard updatePlayBoard(PlayBoard playBoard, Piece piece, int toCol, int toRow) {
        PlayBoard updatePlayBoard = new PlayBoard(cloneStateArray(playBoard.getState()));

        updatePlayBoard.getState()[piece.getCurrentCol()][piece.getCurrentRow()] = null;

        Piece updatedPiece = new Piece(piece);
        updatedPiece.setCurrentCol(toCol);
        updatedPiece.setCurrentRow(toRow);

        updatePlayBoard.getState()[toCol][toRow] = updatedPiece;

        return updatePlayBoard;
    }

    private static Piece[][] cloneStateArray(Piece[][] state) {
        return Arrays.stream(state)
                .map(Piece[]::clone)
                .toArray(Piece[][]::new);
    }

}
