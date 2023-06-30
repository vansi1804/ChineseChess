package com.service;

import java.util.List;

import com.data.dto.TrainingDTO;

public interface TrainingService {

    List<TrainingDTO> findAllChildrenById(Long id);

    TrainingDTO create(TrainingDTO trainingDTO);

    TrainingDTO update(long id, TrainingDTO trainingDTO);

}
