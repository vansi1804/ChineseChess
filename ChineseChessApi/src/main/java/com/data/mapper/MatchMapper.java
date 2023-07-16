package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.match.MatchDetailDTO;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.entity.Match;

@Mapper(componentModel = "spring", uses = { PlayerMapper.class })
public interface MatchMapper {
    
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "player1Id", target = "player1.id")
    @Mapping(source = "player2Id", target = "player2.id")
    @Mapping(ignore = true, target = "result")
    @Mapping(ignore = true, target = "createdDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedDate")
    @Mapping(ignore = true, target = "lastModifiedBy")
    Match toEntity(MatchCreationDTO matchCreationDTO);

    @Mapping(source = "player1", target = "player1ProfileDTO")
    @Mapping(source = "player2", target = "player2ProfileDTO")
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedByUserId")
    MatchDTO toDTO(Match match);

    @Mapping(expression = "java(toDTO(match))", target = "matchDTO")
    @Mapping(ignore = true, target = "moveHistoryDTOs")
    @Mapping(ignore = true, target = "totalTurn")
    MatchDetailDTO toDetailDTO(Match match);

}
