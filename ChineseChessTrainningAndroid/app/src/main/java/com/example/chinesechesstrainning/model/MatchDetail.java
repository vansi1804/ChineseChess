package com.example.chinesechesstrainning.model;

import java.io.Serializable;

public class MatchDetail implements Serializable {
    private int matchId;
    private int turn;
    private int piece_id;
    private int current_col;
    private int current_row;
    private int next_col;
    private int next_row;

    public MatchDetail(int matchId, int turn, int piece_id, int current_col, int current_row, int next_col, int next_row) {
        this.matchId = matchId;
        this.turn = turn;
        this.piece_id = piece_id;
        this.current_col = current_col;
        this.current_row = current_row;
        this.next_col = next_col;
        this.next_row = next_row;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(int piece_id) {
        this.piece_id = piece_id;
    }

    public int getCurrent_col() {
        return current_col;
    }

    public void setCurrent_col(int current_col) {
        this.current_col = current_col;
    }

    public int getCurrent_row() {
        return current_row;
    }

    public void setCurrent_row(int current_row) {
        this.current_row = current_row;
    }

    public int getNext_col() {
        return next_col;
    }

    public void setNext_col(int next_col) {
        this.next_col = next_col;
    }

    public int getNext_row() {
        return next_row;
    }

    public void setNext_row(int next_row) {
        this.next_row = next_row;
    }
}