package com.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.match.MatchDetailDTO;

public interface MatchService {
    
    List<MatchDTO> findAll();

    MatchDTO findById(long id);

    List<MatchDTO> findAllByPlayerId(long playerId);

    MatchDetailDTO findDetailById(long id);

    @Transactional
    MatchDTO create(MatchCreationDTO matchCreationDTO);

    @Transactional
    MatchDTO updateResult(long id, Boolean isRedWin);

}
