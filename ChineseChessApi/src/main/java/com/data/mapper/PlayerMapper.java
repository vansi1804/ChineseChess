package com.data.mapper;

import java.security.NoSuchAlgorithmException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.PlayerDTO;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.data.entity.Player;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PlayerMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "userCreationDTO", target = "user")
    @Mapping(ignore = true, target = "levels")
    @Mapping(ignore = true, target = "elo")
    Player toEntity(PlayerCreationDTO playerCreationDTO) throws NoSuchAlgorithmException;

    @Mapping(source = "userDTO", target = "user")
    @Mapping(ignore = true, target = "levels")
    Player toEntity(PlayerDTO playerDTO);

    @Mapping(source = "user", target = "userProfileDTO")
    @Mapping(source = "levels.name", target = "levelsName")
    PlayerProfileDTO toProfileDTO(Player player);

    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "levels.name", target = "levelsName")
    PlayerDTO toDTO(Player player);

}
