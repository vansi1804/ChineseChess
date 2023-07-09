package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.PlayerDTO;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.data.entity.Player;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PlayerMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "userCreationDTO", target = "user")
    @Mapping(ignore = true, target = "rank")
    @Mapping(ignore = true, target = "elo")
    @Mapping(ignore = true, target = "win")
    @Mapping(ignore = true, target = "draw")
    @Mapping(ignore = true, target = "lose")
    Player toEntity(PlayerCreationDTO playerCreationDTO);

    @Mapping(source = "userProfileDTO", target = "user")
    @Mapping(ignore = true, target = "rank")
    @Mapping(ignore = true, target = "elo")
    @Mapping(ignore = true, target = "win")
    @Mapping(ignore = true, target = "draw")
    @Mapping(ignore = true, target = "lose")
    Player toEntity(PlayerProfileDTO playerProfileDTO);

    @Mapping(source = "player.user", target = "userProfileDTO")
    @Mapping(source = "player.rank.name", target = "rankName")
    PlayerProfileDTO toProfileDTO(Player player);

    @Mapping(source = "player.user", target = "userDTO")
    @Mapping(source = "player.rank.name", target = "rankName")
    PlayerDTO toDTO(Player player);
    
}
