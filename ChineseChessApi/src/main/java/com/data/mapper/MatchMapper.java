package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchDetailDTO;
import com.data.entity.Match;

@Mapper(componentModel = "spring", uses = { PlayerMapper.class })
public interface MatchMapper {
    
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "player1Id", target = "player1.id")
    @Mapping(source = "player2Id", target = "player2.id")
    @Mapping(ignore = true, target = "result")
    @Mapping(ignore = true, target = "startAt")
    @Mapping(ignore = true, target = "stopAt")
    Match toEntity(MatchCreationDTO matchCreationDTO);

    @Mapping(source = "player1", target = "player1ProfileDTO")
    @Mapping(source = "player2", target = "player2ProfileDTO")
    MatchDTO toDTO(Match match);

    @Mapping(expression = "java(toDTO(match))", target = "matchDTO")
    @Mapping(ignore = true, target = "moveHistoryDTOs")
    @Mapping(ignore = true, target = "totalTurn")
    MatchDetailDTO toDetailDTO(Match match);

}
