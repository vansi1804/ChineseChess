package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.LevelsDTO;
import com.data.entity.Levels;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class})
public interface LevelsMapper {
    @Mapping(ignore = true, target = "players")
    Levels toEntity(LevelsDTO levelsDTO);

    @Mapping(source = "players", target = "playerDTOs")
    LevelsDTO toDTO(Levels levels);
}
