package com.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.training.TrainingDTO;
import com.data.dto.training.TrainingDetailDTO;

public interface TrainingService {

    List<TrainingDTO> findAllBase();
    
    TrainingDTO findById(long id);

    TrainingDetailDTO findDetailById(long id);

    TrainingDTO create(TrainingDTO trainingDTO);

    TrainingDTO update(long id, TrainingDTO trainingDTO);

    @Transactional
    boolean deleteById(long id);

}
