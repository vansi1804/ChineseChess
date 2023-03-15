package com.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.dto.LevelsDTO;
import com.entity.Levels;
import com.exception.ExceptionCustom;
import com.mapper.LevelsMapper;
import com.repository.LevelsRepository;
import com.service.LevelsService;

@Service
public class LevelsServiceImpl implements LevelsService {
    @Autowired
    private LevelsRepository levelsRepository;
    @Autowired
    private LevelsMapper levelsMapper;

    @Override
    public List<LevelsDTO> findAll() {
        return levelsRepository.findAll().stream().map(r -> levelsMapper.toDTO(r)).collect(Collectors.toList());
    }

    @Override
    public LevelsDTO findById(int id) {
        return levelsMapper.toDTO(levelsRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustom("Levels", ErrorMessage.DATA_NOT_FOUND, "id", id)));
    }

    @Override
    public LevelsDTO create(LevelsDTO levelsDTO) {
        if (levelsRepository.findByName(levelsDTO.getName()).isPresent())
            throw new ExceptionCustom("Levels", ErrorMessage.EXISTING_NAME, "name", levelsDTO.getName());
        levelsRepository.save(levelsMapper.toEntity(levelsDTO));
        levelsDTO.setId((int) levelsRepository.count());
        return levelsDTO;
    }

    @Override
    public LevelsDTO update(int id, LevelsDTO levelsDTO) {
        Optional<Levels> levels = levelsRepository.findByName(levelsDTO.getName());
        if (levels.isPresent() && (levels.get().getId() != id))
            throw new ExceptionCustom("Levels", ErrorMessage.EXISTING_NAME, "name", levelsDTO.getName());
        levelsRepository.save(levelsMapper.toEntity(levelsDTO));
        return levelsDTO;
    }

}
