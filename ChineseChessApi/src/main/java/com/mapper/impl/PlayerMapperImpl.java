package com.mapper.impl;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.dto.playerDTO.PlayerDTO;
import com.dto.playerDTO.PlayerRegisterByEmailDTO;
import com.dto.playerDTO.PlayerRegisterByPhoneNumberDTO;
import com.entity.Player;
import com.helper.Encoding;
import com.mapper.PlayerMapper;

@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public Player toEntity(PlayerRegisterByEmailDTO playerRegisterByEmailDTO) throws NoSuchAlgorithmException {
        if (playerRegisterByEmailDTO == null)
            return null;
        Player player = new Player();
        player.setEmail(playerRegisterByEmailDTO.getEmail());
        player.setPassword(Encoding.getMD5(playerRegisterByEmailDTO.getPassword()));
        return player;
    }

    @Override
    public Player toEntity(PlayerRegisterByPhoneNumberDTO playerRegisterByPhoneNumberDTO) throws NoSuchAlgorithmException{
        if (playerRegisterByPhoneNumberDTO == null)
            return null;
        Player player = new Player();
        player.setPhoneNumber(playerRegisterByPhoneNumberDTO.getPhoneNumber());
        player.setPassword(Encoding.getMD5(playerRegisterByPhoneNumberDTO.getPassword()));
        return player;
    }

    @Override
    public Player toEntity(PlayerDTO playerDTO) {
        if (playerDTO == null)
            return null;
        Player player = new Player();
        player.setEmail(playerDTO.getEmail());
        player.setPhoneNumber(playerDTO.getPhoneNumber());
        player.setName(playerDTO.getName());
        player.setAvatar(playerDTO.getAvatar());
        player.setEloScore(playerDTO.getEloScore());
        player.setLevelsId(playerDTO.getLevelsDTO().getId());
        player.setRoleId(playerDTO.getRoleDTO().getId());
        return player;
    }

    @Override
    public PlayerDTO toDTO(Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toDTO'");
    }

}
