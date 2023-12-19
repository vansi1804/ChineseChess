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
    
    @Mappings({
       
    })
    
    Match toEntity(MatchCreationDTO matchCreationDTO);

    @Mapping(source = "player1", target = "player1ProfileDTO")
    @Mapping(source = "player2", target = "player2ProfileDTO")
    @Mapping(expression = "java(toOthersInfoDTO(match))", target = "matchOthersInfoDTO")
    MatchDTO toDTO(Match match);

    @Mapping(expression = "java(toDTO(match))", target = "matchDTO")
    @Mapping(ignore = true, target = "moveHistoryDTOs")
    @Mapping(ignore = true, target = "totalTurn")
    MatchDetailDTO toDetailDTO(Match match);

    MatchOthersInfoDTO toOthersInfoDTO(Match match);

}
