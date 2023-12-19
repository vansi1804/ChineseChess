package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.data.dto.match.MatchDetailDTO;
import com.data.dto.match.MatchOthersInfoDTO;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.entity.Match;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface MatchMapper {
    
    // Ignoring some fields with comments explaining why they're ignored
    @Mappings({
        @Mapping(ignore = true, target = "id"), // Handled by the database
        @Mapping(ignore = true, target = "result"), // Dynamically calculated
        @Mapping(ignore = true, target = "createdDate"), // Handled by auditing
        @Mapping(ignore = true, target = "createdByUserId"), // Handled by auditing
        @Mapping(ignore = true, target = "lastModifiedDate"), // Handled by auditing
        @Mapping(ignore = true, target = "lastModifiedByUserId") // Handled by auditing
    })
    Match toEntity(MatchCreationDTO matchCreationDTO);

    // Mapping expressions extracted into methods for readability
    @Mappings({
        @Mapping(source = "player1", target = "player1ProfileDTO"),
        @Mapping(source = "player2", target = "player2ProfileDTO"),
        @Mapping(expression = "java(toOthersInfoDTO(match))", target = "matchOthersInfoDTO")
    })
    MatchDTO toDTO(Match match);

    @Mappings({
        @Mapping(expression = "java(toDTO(match))", target = "matchDTO"),
        @Mapping(ignore = true, target = "moveHistoryDTOs"),
        @Mapping(ignore = true, target = "totalTurn")
    })
    MatchDetailDTO toDetailDTO(Match match);

    MatchOthersInfoDTO toOthersInfoDTO(Match match);
}
