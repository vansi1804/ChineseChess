package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.data.dto.TrainingDTO;
import com.data.dto.TrainingDetailDTO;
import com.data.entity.Training;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(source = "parentTrainingId", target = "parentTraining", qualifiedByName = "mapParentTrainingToEntity")
    @Mapping(ignore = true, target = "childTrainings")
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    Training toEntity(TrainingDTO trainingDTO);

    @Named("mapParentTrainingToEntity")
    default Training mapParentTrainingToEntity(Long parentTrainingId) {
        if (parentTrainingId == null) {
            return null;
        } else {
            Training parentTraining = new Training();
            parentTraining.setId(parentTrainingId);
            return parentTraining;
        }
    }

    @Mapping(source = "parentTraining.id", target = "parentTrainingId")
    @Mapping(source = "childTrainings", target = "childTrainingDTOs")
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedByUserId")
    TrainingDTO toDTO(Training training);

    @Mapping(expression = "java(toDTO(training))", target = "trainingDTO")
    @Mapping(ignore = true, target = "moveHistoryDTOs")
    @Mapping(ignore = true, target = "totalTurn")
    TrainingDetailDTO toDetailDTO(Training training);

}
