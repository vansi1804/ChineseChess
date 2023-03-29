package com.service;

import java.util.List;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;

public interface MatchService {
    List<MatchDTO> findAll();

    MatchDTO findById(long id);

    List<MatchDTO> findAllByPlayerId(long playerId);

    MatchDTO create(MatchCreationDTO matchCreationDTO);
}
