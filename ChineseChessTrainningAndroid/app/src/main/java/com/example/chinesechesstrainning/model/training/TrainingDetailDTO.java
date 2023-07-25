package com.example.chinesechesstrainning.model.training;

import com.example.chinesechesstrainning.model.move.MoveHistoryDTO;

import java.io.Serializable;
import java.util.Map;

public class TrainingDetailDTO implements Serializable {
    private TrainingDTO trainingDTO;
    private int totalTurn;
    private Map<Long, MoveHistoryDTO> moveHistoryDTOs;

    public TrainingDetailDTO() {
    }

    public TrainingDetailDTO(TrainingDTO trainingDTO, int totalTurn, Map<Long, MoveHistoryDTO> moveHistoryDTOs) {
        this.trainingDTO = trainingDTO;
        this.totalTurn = totalTurn;
        this.moveHistoryDTOs = moveHistoryDTOs;
    }

    public TrainingDTO getTrainingDTO() {
        return trainingDTO;
    }

    public void setTrainingDTO(TrainingDTO trainingDTO) {
        this.trainingDTO = trainingDTO;
    }

    public int getTotalTurn() {
        return totalTurn;
    }

    public void setTotalTurn(int totalTurn) {
        this.totalTurn = totalTurn;
    }

    public Map<Long, MoveHistoryDTO> getMoveHistoryDTOs() {
        return moveHistoryDTOs;
    }

    public void setMoveHistoryDTOs(Map<Long, MoveHistoryDTO> moveHistoryDTOs) {
        this.moveHistoryDTOs = moveHistoryDTOs;
    }
}