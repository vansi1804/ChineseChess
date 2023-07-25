package com.example.chinesechesstrainning.model.training;

import java.io.Serializable;
import java.util.List;

public class TrainingDTO implements Serializable {
    private Long id;
    private String title;
    private Long parentTrainingId;
    private List<TrainingDTO> childTrainingDTOS;

    public TrainingDTO() {
    }

    public TrainingDTO(Long id, String title, Long parentTrainingId) {
        this.id = id;
        this.title = title;
        this.parentTrainingId = parentTrainingId;
    }
    public TrainingDTO(Long id, String title, Long parentTrainingId, List<TrainingDTO> childTrainingDTOS) {
        this.id = id;
        this.title = title;
        this.parentTrainingId = parentTrainingId;
        this.childTrainingDTOS = childTrainingDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentTrainingId() {
        return parentTrainingId;
    }

    public void setParentTrainingId(Long parentTrainingId) {
        this.parentTrainingId = parentTrainingId;
    }

    public List<TrainingDTO> getChildTrainings() {
        return childTrainingDTOS;
    }

    public void setChildTrainings(List<TrainingDTO> childTrainingDTOS) {
        this.childTrainingDTOS = childTrainingDTOS;
    }
}
