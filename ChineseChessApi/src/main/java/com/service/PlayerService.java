package com.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.data.dto.PlayerDTO;
import com.common.enumeration.EResult;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;

public interface PlayerService {
    
    Page<PlayerDTO> findAll(int no, int limit, String sortBy);

    PlayerDTO findByUserId(long id);

    PlayerProfileDTO findById(long id);

    @Transactional
    PlayerDTO create(PlayerCreationDTO playerCreationDTO, MultipartFile fileAvatar);

    @Transactional
    PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO, MultipartFile fileAvatar);

    @Transactional
    PlayerProfileDTO updateByEloBetAndResult(long id, int eloBet, EResult eResult);

}
