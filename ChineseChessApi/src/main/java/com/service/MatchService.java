package com.service;

import java.util.List;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchDetailDTO;
import com.data.dto.MatchStartDTO;
import com.data.dto.TrainingMatchDTO;

public interface MatchService {
    List<MatchDTO> findAll();

    MatchDTO findById(long id);

    List<MatchDTO> findAllByPlayerId(long playerId);

    MatchDetailDTO findDetailById(long id);

    MatchStartDTO create(MatchCreationDTO matchCreationDTO);

    MatchDTO updateResult(long id, Boolean isRedWin);

    TrainingMatchDTO create(long trainingId);

}
