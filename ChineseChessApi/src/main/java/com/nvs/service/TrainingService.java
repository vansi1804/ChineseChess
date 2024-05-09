package com.nvs.service;

import com.nvs.data.dto.training.TrainingDTO;
import com.nvs.data.dto.training.TrainingDetailDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface TrainingService {
  List<TrainingDTO> findAllBase();

  TrainingDTO findById(long id);

  TrainingDetailDTO findDetailById(long id);

  TrainingDTO create(TrainingDTO trainingDTO);

  TrainingDTO update(long id, TrainingDTO trainingDTO);

  @Transactional
  boolean deleteById(long id);
}
