package com.example.chinesechesstrainning.model;

public class Move {
    private Piece movingPiece;
    private PlayBoard updatedPlayBoard;

    public Move() {
    }

    public Move(Piece movingPiece, PlayBoard currentPlayBoard) {
        this.movingPiece = movingPiece;
        this.updatedPlayBoard = currentPlayBoard;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public PlayBoard getUpdatedPlayBoard() {
        return updatedPlayBoard;
    }

    public void setUpdatedPlayBoard(PlayBoard updatedPlayBoard) {
        this.updatedPlayBoard = updatedPlayBoard;
    }
}
