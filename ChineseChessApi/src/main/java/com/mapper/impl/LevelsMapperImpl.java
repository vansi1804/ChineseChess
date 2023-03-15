package com.mapper.impl;

import org.springframework.stereotype.Component;

import com.dto.LevelsDTO;
import com.entity.Levels;
import com.mapper.LevelsMapper;

@Component
public class LevelsMapperImpl implements LevelsMapper {

    @Override
    public Levels toEntity(LevelsDTO levelsDTO) {
        if (levelsDTO == null)
            return null;
        Levels levels = new Levels();
        levels.setId(levelsDTO.getId());
        levels.setName(levelsDTO.getName());
        return levels;
    }

    @Override
    public LevelsDTO toDTO(Levels levels) {
        if (levels == null)
            return null;
        LevelsDTO levelsDTO = new LevelsDTO();
        levelsDTO.setId(levels.getId());
        levelsDTO.setName(levels.getName());
        return levelsDTO;
    }

}
