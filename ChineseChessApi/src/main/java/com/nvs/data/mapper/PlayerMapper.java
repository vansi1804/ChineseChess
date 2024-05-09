package com.nvs.data.mapper;

import com.nvs.data.dto.player.PlayerCreationDTO;
import com.nvs.data.dto.player.PlayerDTO;
import com.nvs.data.dto.player.PlayerOthersInfoDTO;
import com.nvs.data.dto.player.PlayerProfileDTO;
import com.nvs.data.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class, RankMapper.class })
public interface PlayerMapper {
        @Mapping(ignore = true, target = "id")
        @Mapping(source = "userCreationDTO", target = "user")
        @Mapping(ignore = true, target = "rank")
        @Mapping(ignore = true, target = "elo")
        @Mapping(ignore = true, target = "win")
        @Mapping(ignore = true, target = "draw")
        @Mapping(ignore = true, target = "lose")
        Player toEntity(PlayerCreationDTO playerCreationDTO);

        @Mapping(ignore = true, target = "id")
        @Mapping(source = "userProfileDTO", target = "user")
        @Mapping(ignore = true, target = "rank")
        @Mapping(ignore = true, target = "elo")
        @Mapping(ignore = true, target = "win")
        @Mapping(ignore = true, target = "draw")
        @Mapping(ignore = true, target = "lose")
        Player toEntity(PlayerProfileDTO playerProfileDTO);

        @Mapping(source = "player.user", target = "userProfileDTO")
        @Mapping(expression = "java(toOthersInfoDTO(player))", target = "playerOthersInfoDTO")
        PlayerProfileDTO toProfileDTO(Player player);

        @Mapping(source = "player.user", target = "userDTO")
        @Mapping(expression = "java(toOthersInfoDTO(player))", target = "playerOthersInfoDTO")
        PlayerDTO toDTO(Player player);

        @Mapping(source = "rank", target = "rankDTO")
        PlayerOthersInfoDTO toOthersInfoDTO(Player player);
}
