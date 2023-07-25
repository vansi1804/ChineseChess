package com.example.chinesechesstrainning.model;

public class PlayBoardDTO {
    private PieceDTO[][] state;

    public PlayBoardDTO(PieceDTO[][] state) {
        this.state = state;
    }

    public PlayBoardDTO() {
        state = new PieceDTO[9][10];
    }

    public PieceDTO[][] getState() {
        return state;
    }

    public void setState(PieceDTO[][] state) {
        this.state = state;
    }
}
