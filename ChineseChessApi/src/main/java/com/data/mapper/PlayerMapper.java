package com.data.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.PlayerDTO;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.data.entity.Match;
import com.data.entity.Player;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PlayerMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "userCreationDTO", target = "user")
    @Mapping(ignore = true, target = "rank")
    @Mapping(target = "elo", constant = "1000")
    Player toEntity(PlayerCreationDTO playerCreationDTO);

    @Mapping(source = "userProfileDTO", target = "user")
    @Mapping(ignore = true, target = "rank")
    @Mapping(ignore = true, target = "elo")
    Player toEntity(PlayerProfileDTO playerProfileDTO);

    @Mapping(source = "player.user", target = "userProfileDTO")
    @Mapping(source = "player.rank.name", target = "rankName")
    @Mapping(target = "win", expression = "java(getStatistics(player,matches)[0])")
    @Mapping(target = "lost", expression = "java(getStatistics(player,matches)[1])")
    @Mapping(target = "draw", expression = "java(getStatistics(player,matches)[2])")
    PlayerProfileDTO toProfileDTO(Player player, List<Match> matches);

    @Mapping(source = "player.user", target = "userDTO")
    @Mapping(source = "player.rank.name", target = "rankName")
    @Mapping(target = "win", expression = "java(getStatistics(player,matches)[0])")
    @Mapping(target = "lost", expression = "java(getStatistics(player,matches)[1])")
    @Mapping(target = "draw", expression = "java(getStatistics(player,matches)[2])")
    PlayerDTO toDTO(Player player, List<Match> matches);

    default int[] getStatistics(Player player, List<Match> matches) {
        if (matches == null || matches.isEmpty()) {
            return new int[] { 0, 0, 0 };
        }
        int win = 0;
        int lost = 0;
        int draw = 0;
        for (Match match : matches) {
            if (match.getResult() != null) {
                if (match.getResult() == 0) {
                    draw++;
                } else if (match.getResult() == player.getId()) {
                    win++;
                } else {
                    lost++;
                }
            }
        }
        return new int[] { win, lost, draw };
    }
    
}
