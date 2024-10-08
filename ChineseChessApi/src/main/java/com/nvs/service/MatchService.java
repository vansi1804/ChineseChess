package com.nvs.service;

import com.nvs.data.dto.match.MatchCreationDTO;
import com.nvs.data.dto.match.MatchDTO;
import com.nvs.data.dto.match.MatchDetailDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface MatchService{

   List<MatchDTO> findAll();

   MatchDTO findById(long id);

   List<MatchDTO> findAllByPlayerId(long playerId);

   MatchDetailDTO findDetailById(long id);

   @Transactional
   MatchDTO create(MatchCreationDTO matchCreationDTO);

   @Transactional
   MatchDTO updateResult(long id, Boolean result);

}
