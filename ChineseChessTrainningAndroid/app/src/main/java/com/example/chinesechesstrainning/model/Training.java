package com.example.chinesechesstrainning.model;

import java.io.Serializable;
import java.util.List;

public class Training implements Serializable {
    private Long id;
    private String name;
    private Long parentTrainingId;

    public Training() {
    }
    public Training(Long id, String name, Long parentTrainingId) {
        this.id = id;
        this.name = name;
        this.parentTrainingId = parentTrainingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentTrainingId() {
        return parentTrainingId;
    }

    public void setParentTrainingId(Long parentTrainingId) {
        this.parentTrainingId = parentTrainingId;
    }
}
