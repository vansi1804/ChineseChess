package com.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.RankDTO;

public interface RankService {

    List<RankDTO> findAll();

    RankDTO findById(int id);

    RankDTO findByName(String name);

    RankDTO create(RankDTO rankDTO);

    RankDTO update(int id, RankDTO rankDTO);
    
    @Transactional
    boolean delete(int id);
    
}
