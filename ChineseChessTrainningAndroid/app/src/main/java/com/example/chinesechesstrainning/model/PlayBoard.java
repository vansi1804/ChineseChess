package com.example.chinesechesstrainning.model;

public class PlayBoard {
    private Piece[][] state;

    public PlayBoard(Piece[][] state) {
        this.state = state;
    }

    public PlayBoard() {
        state = new Piece[9][10];
    }

    public Piece[][] getState() {
        return state;
    }

    public void setState(Piece[][] state) {
        this.state = state;
    }
}
