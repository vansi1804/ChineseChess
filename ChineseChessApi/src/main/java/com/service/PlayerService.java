package com.service;

import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerService {
  Page<PlayerDTO> findAll(int no, int limit, String sortBy);

  PlayerDTO findByUserId(long id);

  PlayerProfileDTO findById(long id);

  @Transactional
  PlayerDTO create(PlayerCreationDTO playerCreationDTO);

  @Transactional
  PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO);

  @Transactional
  PlayerProfileDTO updateByMatchResult(long id, int result, int eloBet);
}
