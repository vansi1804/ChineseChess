package com.mapper;

import org.mapstruct.Mapper;

import com.dto.LevelsDTO;
import com.entity.Levels;

@Mapper(componentModel = "spring")
public interface LevelsMapper {
    Levels toEntity(LevelsDTO levelsDTO);

    LevelsDTO toDTO(Levels levels);
}
