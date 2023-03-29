package com.service;

import java.util.List;

import com.data.dto.LevelsDTO;

public interface LevelsService {
    List<LevelsDTO> findAll();

    LevelsDTO findById(int id);

    LevelsDTO findByName(String name);

    LevelsDTO create(LevelsDTO levelsDTO);

    LevelsDTO update(int id, LevelsDTO levelsDTO);
    
    void delete(int id);
}
