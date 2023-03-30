package com.service;

import java.util.List;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchStartDTO;

public interface MatchService {
    List<MatchDTO> findAll();

    MatchDTO findById(long id);

    List<MatchDTO> findAllByPlayerId(long playerId);

    MatchStartDTO create(MatchCreationDTO matchCreationDTO);
}
