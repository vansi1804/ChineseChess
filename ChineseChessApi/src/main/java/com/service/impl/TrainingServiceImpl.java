package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.training.TrainingDTO;
import com.data.dto.training.TrainingDetailDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.TrainingMapper;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.config.exception.ConflictExceptionCustomize;
import com.config.exception.InvalidExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.service.MoveService;
import com.service.TrainingService;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveService moveService;

    public TrainingServiceImpl(
            TrainingMapper trainingMapper,
            TrainingRepository trainingRepository,
            MoveService moveService,
            MoveHistoryRepository moveHistoryRepository) {

        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.moveHistoryRepository = moveHistoryRepository;
        this.moveService = moveService;
    }

    @Override
    public List<TrainingDTO> findAllBase() {
        return trainingRepository.findAllBase().stream()
                .map(t -> trainingMapper.toDTO(t))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDTO findById(long id) {
        return trainingRepository.findById(id)
                .map(t -> trainingMapper.toDTO(t))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public TrainingDTO create(TrainingDTO trainingDTO) {
        if ((trainingDTO.getParentTrainingId() != null)
                && !trainingRepository.existsById(trainingDTO.getParentTrainingId())) {

            throw new ResourceNotFoundExceptionCustomize(
                    Collections.singletonMap("parentTrainingId", trainingDTO.getParentTrainingId()));
        }

        if (trainingRepository.existByParentTrainingIdAndTitle(
                trainingDTO.getParentTrainingId(), trainingDTO.getTitle())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
            errors.put("title", trainingDTO.getTitle());

            throw new ConflictExceptionCustomize(errors);
        }

        return trainingMapper.toDTO(trainingRepository.save(trainingMapper.toEntity(trainingDTO)));
    }

    @Override
    public TrainingDTO update(long id, TrainingDTO trainingDTO) {
        Training oldTraining = trainingRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));

        if ((trainingDTO.getParentTrainingId() != null)
                && !trainingRepository.existsById(trainingDTO.getParentTrainingId())) {

            throw new ResourceNotFoundExceptionCustomize(
                    Collections.singletonMap("parentTrainingId", trainingDTO.getParentTrainingId()));
        }

        if (trainingDTO.getParentTrainingId() == oldTraining.getId()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("id", oldTraining.getId());
            errors.put("parentTrainingId", trainingDTO.getParentTrainingId());

            throw new InvalidExceptionCustomize(errors);
        }

        if (trainingRepository.existByParentTrainingIdAndTitle(
                trainingDTO.getParentTrainingId(), trainingDTO.getTitle())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
            errors.put("title", trainingDTO.getTitle());

            throw new ConflictExceptionCustomize(errors);
        }

        Training updateTraining = trainingMapper.toEntity(trainingDTO);
        updateTraining.setId(oldTraining.getId());

        return trainingMapper.toDTO(trainingRepository.save(updateTraining));
    }

    @Override
    public boolean deleteById(long id) {
        if (!trainingRepository.existsById(id)) {
            throw new ResourceNotFoundExceptionCustomize(
                    Collections.singletonMap("id", id));
        }

        moveHistoryRepository.deleteAllByTraining_Id(id);
        trainingRepository.deleteById(id);

        return true;
    }

    @Override
    public TrainingDetailDTO findDetailById(long id) {
        TrainingDetailDTO trainingDetailDTO = trainingRepository.findById(id)
                .map(m -> trainingMapper.toDetailDTO(m))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(id);
        Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.build(moveHistories);
        trainingDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        trainingDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

        return trainingDetailDTO;
    }

}
