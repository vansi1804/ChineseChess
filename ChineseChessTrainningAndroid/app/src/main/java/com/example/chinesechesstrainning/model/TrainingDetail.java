package com.example.chinesechesstrainning.model;

import java.io.Serializable;
import java.util.Map;

public class TrainingDetail implements Serializable {
    private Training training;
    private int totalTurn;
    private Map<Integer, Move> moves;

    public TrainingDetail() {
    }

    public TrainingDetail(Training training, int totalTurn, Map<Integer, Move> move) {
        this.training = training;
        this.totalTurn = totalTurn;
        this.moves = move;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public int getTotalTurn() {
        return totalTurn;
    }

    public void setTotalTurn(int totalTurn) {
        this.totalTurn = totalTurn;
    }

    public Map<Integer, Move> getMoves() {
        return moves;
    }

    public void setMoves(Map<Integer, Move> moves) {
        this.moves = moves;
    }
}