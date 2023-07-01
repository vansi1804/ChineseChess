package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchDetailDTO;
import com.data.dto.MatchStartDTO;
import com.data.entity.Match;

@Mapper(componentModel = "spring", uses = { TrainingMapper.class })
public interface MatchMapper {
    
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "player1Id", target = "player1.id")
    @Mapping(source = "player2Id", target = "player2.id")
    @Mapping(ignore = true, target = "training")
    @Mapping(ignore = true, target = "result")
    @Mapping(ignore = true, target = "startAt")
    @Mapping(ignore = true, target = "stopAt")
    Match toEntity(MatchCreationDTO matchCreationDTO);

    @Mapping(source = "player1.id", target = "player1Id")
    @Mapping(source = "player1.user.name", target = "player1Name")
    @Mapping(source = "player1.user.avatar", target = "player1Avatar")
    @Mapping(source = "player2.id", target = "player2Id")
    @Mapping(source = "player2.user.name", target = "player2Name")
    @Mapping(source = "player2.user.avatar", target = "player2Avatar")
    MatchDTO toDTO(Match match);

    @Mapping(expression = "java(toDTO(match))", target = "matchDTO")
    @Mapping(ignore = true, target = "playBoardStartDTO")
    MatchStartDTO toStartDTO(Match match);

    @Mapping(expression = "java(toDTO(match))", target = "matchDTO")
    @Mapping(ignore = true, target = "moveHistoryDTOs")
    @Mapping(ignore = true, target = "totalTurn")
    MatchDetailDTO toDetailDTO(Match match);

}
