package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.MoveHistoryDTO;
import com.data.dto.TrainingDTO;
import com.data.dto.TrainingDetailDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.TrainingMapper;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.exception.ConflictException;
import com.exception.ResourceNotFoundException;
import com.service.MoveHistoryService;
import com.service.TrainingService;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveHistoryService moveHistoryService;

    @Autowired
    public TrainingServiceImpl(TrainingMapper trainingMapper,
            TrainingRepository trainingRepository,
            MoveHistoryService moveHistoryService,
            MoveHistoryRepository moveHistoryRepository) {
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.moveHistoryRepository = moveHistoryRepository;
        this.moveHistoryService = moveHistoryService;
    }

    @Override
    public List<TrainingDTO> findAllChildrenById(Long id) {
        return trainingRepository.findAllByParentTraining_Id(id).stream()
                .map(t -> trainingMapper.toDTO(t))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDTO findById(long id) {
        return trainingRepository.findById(id).map(t -> trainingMapper.toDTO(t))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("id", id)));
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

    @Override
    public boolean deleteById(long id) {
        if (!trainingRepository.existsById(id)) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        moveHistoryRepository.deleteAllByTraining_Id(id);
        trainingRepository.deleteById(id);

        return true;
    }

    @Override
    public TrainingDetailDTO findDetailById(long id) {
        TrainingDetailDTO trainingDetailDTO = trainingRepository.findById(id)
                .map(m -> trainingMapper.toDetailDTO(m))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(id);
        List<MoveHistoryDTO> moveHistoryDTOs = moveHistoryService.build(moveHistories);

        for (MoveHistoryDTO moveHistoryDTO : moveHistoryDTOs) {
            System.out.println("\n" + moveHistoryDTO.getTurn() + ":\t" + moveHistoryDTO.getDescription());
            moveHistoryDTO.getCurrentBoard().print(moveHistoryDTO.getMovingPieceDTO());
        }

        trainingDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        trainingDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);
        return trainingDetailDTO;
    }

}
