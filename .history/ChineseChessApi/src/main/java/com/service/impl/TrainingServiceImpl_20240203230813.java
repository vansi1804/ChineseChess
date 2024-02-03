package com.service.impl;

import com.config.exception.ConflictExceptionCustomize;
import com.config.exception.InvalidExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.training.TrainingDTO;
import com.data.dto.training.TrainingDetailDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.TrainingMapper;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.service.MoveService;
import com.service.TrainingService;
import com.service.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

  private final TrainingMapper trainingMapper;
  private final TrainingRepository trainingRepository;
  private final MoveHistoryRepository moveHistoryRepository;
  private final MoveService moveService;
  private final UserService userService;

  @Override
  public List<TrainingDTO> findAllBase() {
    return trainingRepository
      .findAllBase()
      .stream()
      .map(t -> trainingMapper.toDTO(t))
      .collect(Collectors.toList());
  }

  @Override
  public TrainingDTO findById(long id) {
    return trainingRepository
      .findById(id)
      .map(t -> trainingMapper.toDTO(t))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );
  }

  @Override
  public TrainingDTO create(TrainingDTO trainingDTO) {
    if (
      (trainingDTO.getParentTrainingId() != null) &&
      !trainingRepository.existsById(trainingDTO.getParentTrainingId())
    ) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap(
          "parentTrainingId",
          trainingDTO.getParentTrainingId()
        )
      );
    }

    if (
      trainingRepository.existByParentTrainingIdAndTitle(
        trainingDTO.getParentTrainingId(),
        trainingDTO.getTitle()
      )
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
      errors.put("title", trainingDTO.getTitle());

      throw new ConflictExceptionCustomize(errors);
    }

    return trainingMapper.toDTO(
      trainingRepository.save(trainingMapper.toEntity(trainingDTO))
    );
  }

  @Override
  public TrainingDTO update(long id, TrainingDTO trainingDTO) {
    Training existingTraining = trainingRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (!userService.isCurrentUser(existingTraining.getCreatedByUserId())) {
      throw new AccessDeniedException(null);
    }

    if (
      (trainingDTO.getParentTrainingId() != null) &&
      !trainingRepository.existsById(trainingDTO.getParentTrainingId())
    ) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap(
          "parentTrainingId",
          trainingDTO.getParentTrainingId()
        )
      );
    }

    if (
      Objects.equals(
        trainingDTO.getParentTrainingId(),
        existingTraining.getId()
      )
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("id", existingTraining.getId());
      errors.put("parentTrainingId", trainingDTO.getParentTrainingId());

      throw new InvalidExceptionCustomize(errors);
    }

    if (
      trainingRepository.existByParentTrainingIdAndTitle(
        trainingDTO.getParentTrainingId(),
        trainingDTO.getTitle()
      )
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("parentTrainingId", trainingDTO.getParentTrainingId());
      errors.put("title", trainingDTO.getTitle());

      throw new ConflictExceptionCustomize(errors);
    }

    Training updateTraining = trainingMapper.toEntity(trainingDTO);
    updateTraining.setId(existingTraining.getId());
    updateTraining.setCreatedByUserId(existingTraining.getCreatedByUserId());
    updateTraining.setCreatedDate(existingTraining.getCreatedDate());

    Training updatedTraining = trainingRepository.save(updateTraining);
    trainingRepository.flush();

    return trainingMapper.toDTO(updatedTraining);
  }

  @Override
  public boolean deleteById(long id) {
    Training oldTraining = trainingRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (!userService.isCurrentUser(oldTraining.getCreatedByUserId())) {
      throw new AccessDeniedException(null);
    }

    trainingRepository.delete(oldTraining);

    return true;
  }

  @Override
  public TrainingDetailDTO findDetailById(long id) {
    TrainingDetailDTO trainingDetailDTO = trainingRepository
      .findById(id)
      .map(m -> trainingMapper.toDetailDTO(m))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(
      id
    );
    Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.build(
      moveHistories
    );
    trainingDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
    trainingDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

    return trainingDetailDTO;
  }
}
