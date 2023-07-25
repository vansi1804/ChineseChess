package com.example.chinesechesstrainning.model.move;

public class TrainingMoveCreationDTO extends MoveDetailDTO{

    private Long trainingId;

    public TrainingMoveCreationDTO() {
        super();
    }

    public TrainingMoveCreationDTO(Integer pieceId, Integer toCol, Integer toRow, Long trainingId) {
        super(pieceId, toCol, toRow);
        this.trainingId = trainingId;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }
}
