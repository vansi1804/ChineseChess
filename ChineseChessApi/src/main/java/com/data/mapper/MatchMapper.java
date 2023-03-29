package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.entity.Match;


@Mapper(componentModel = "spring")
public interface MatchMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "player1")
    @Mapping(ignore = true, target = "player2")
    @Mapping(ignore = true, target = "startAt")
    @Mapping(ignore = true, target = "result")
    Match toEntity(MatchCreationDTO matchCreationDTO);

    @Mapping(source = "player1.id", target = "player1Id")
    @Mapping(source = "player1.user.name", target = "player1Name")
    @Mapping(source = "player1.user.avatar", target = "player1Avatar")
    @Mapping(source = "player2.id", target = "player2Id")
    @Mapping(source = "player2.user.name", target = "player2Name")
    @Mapping(source = "player2.user.avatar", target = "player2Avatar")
    @Mapping(ignore = true, target = "playBoard")
    MatchDTO toDTO(Match match);
}
