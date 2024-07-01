package com.nvs.data.mapper;

import com.nvs.data.dto.training.TrainingDTO;
import com.nvs.data.dto.training.TrainingDetailDTO;
import com.nvs.data.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TrainingMapper{
   
   @Mapping(source = "parentTrainingId", target = "parentTraining", qualifiedByName = "mapParentTrainingToEntity")
   Training toEntity(TrainingDTO trainingDTO);

   @Named("mapParentTrainingToEntity")
   default Training mapParentTrainingToEntity(Long parentTrainingId){
      if(parentTrainingId == null){
         return null;
      } else {
         Training parentTraining = new Training();
         parentTraining.setId(parentTrainingId);
         return parentTraining;
      }
   }

   @Mapping(source = "parentTraining.id", target = "parentTrainingId")
   @Mapping(source = "childTrainings", target = "childTrainingDTOs")
   TrainingDTO toDTO(Training training);

   @Mapping(expression = "java(toDTO(training))", target = "trainingDTO")
   TrainingDetailDTO toDetailDTO(Training training);

}
