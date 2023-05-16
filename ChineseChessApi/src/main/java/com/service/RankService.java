package com.service;

import java.util.List;

import com.data.dto.RankDTO;

public interface RankService {
    List<RankDTO> findAll();

    RankDTO findById(int id);

    RankDTO findByName(String name);

    RankDTO create(RankDTO rankDTO);

    RankDTO update(int id, RankDTO rankDTO);
    
    void delete(int id);
}
