package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.TrainingDTO;
import com.data.entity.Training;
import com.data.mapper.TrainingMapper;
import com.data.repository.TrainingRepository;
import com.exception.ConflictException;
import com.exception.ResourceNotFoundException;
import com.service.TrainingService;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingServiceImpl(TrainingMapper trainingMapper, TrainingRepository trainingRepository) {
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public List<TrainingDTO> findAllChildrenById(Long id) {
        return trainingRepository.findAllByParentTraining_Id(id).stream()
                .map(t -> trainingMapper.toDTO(t))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDTO create(TrainingDTO trainingDTO) {
        if (trainingDTO.getParentTrainingId() != null
                && !trainingRepository.existsById(trainingDTO.getParentTrainingId())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("parentTrainingId", trainingDTO.getParentTrainingId()));
        }

        if (trainingRepository.existByParentTrainingIdAndTitle(
                trainingDTO.getParentTrainingId(), trainingDTO.getTitle())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
            errors.put("title", trainingDTO.getTitle());
            throw new ConflictException(errors);
        }

        Training createdTraining = trainingRepository.saveAndFlush(trainingMapper.toEntity(trainingDTO));

        return trainingMapper.toDTO(createdTraining);
    }

    @Override
    public TrainingDTO update(long id, TrainingDTO trainingDTO) {
        if (!trainingRepository.existsById(id)) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        if (trainingRepository.existByParentTrainingIdAndTitle(
                trainingDTO.getParentTrainingId(), trainingDTO.getTitle())) {
                    
            Map<String, Object> errors = new HashMap<>();
            errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
            errors.put("title", trainingDTO.getTitle());
            throw new ConflictException(errors);
        }

        Training updateTraining = trainingMapper.toEntity(trainingDTO);
        updateTraining.setId(id);

        return trainingMapper.toDTO(trainingRepository.saveAndFlush(updateTraining));
    }

}
