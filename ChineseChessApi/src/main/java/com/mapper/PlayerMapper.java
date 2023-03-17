package com.mapper;

import java.security.NoSuchAlgorithmException;

import org.mapstruct.Mapper;

import com.dto.playerDTO.PlayerDTO;
import com.dto.playerDTO.PlayerRegisterByEmailDTO;
import com.dto.playerDTO.PlayerRegisterByPhoneNumberDTO;
import com.entity.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toEntity(PlayerRegisterByEmailDTO playerRegisterByEmailDTO) throws NoSuchAlgorithmException;

    Player toEntity(PlayerRegisterByPhoneNumberDTO playerRegisterByPhoneNumberDTO) throws NoSuchAlgorithmException;

    Player toEntity(PlayerDTO playerDTO);

    PlayerDTO toDTO(Player player);
}
