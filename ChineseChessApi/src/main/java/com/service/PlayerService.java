package com.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.data.dto.PlayerDTO;
import com.data.dto.profile.PlayerProfileDTO;
import com.data.dto.register.PlayerRegisterDTO;

public interface PlayerService {
    List<PlayerDTO> findAll();

    PlayerDTO findById(long id);

    PlayerProfileDTO create(PlayerRegisterDTO playerRegisterDTO) throws NoSuchAlgorithmException;

    PlayerProfileDTO findProfileById(long id);

    PlayerProfileDTO updateProfileById(long id, PlayerProfileDTO playerProfileDTO);

    PlayerProfileDTO updateEloById(long id, int elo);

    PlayerDTO lockById(long id);

    PlayerDTO unlockById(long id);

}