package com.service;

import java.util.List;

import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.match.MatchDetailDTO;

public interface MatchService {
    
    List<MatchDTO> findAll();

    MatchDTO findById(long id);

    List<MatchDTO> findAllByPlayerId(long playerId);

    MatchDetailDTO findDetailById(long id);

    MatchDTO create(MatchCreationDTO matchCreationDTO);

    MatchDTO updateResult(long id, Boolean isRedWin);

}
