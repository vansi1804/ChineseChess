package com.nvs.data.mapper;

import com.nvs.data.dto.match.MatchCreationDTO;
import com.nvs.data.dto.match.MatchDTO;
import com.nvs.data.dto.match.MatchDetailDTO;
import com.nvs.data.dto.match.MatchOthersInfoDTO;
import com.nvs.data.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface MatchMapper {
        @Mapping(ignore = true, target = "id")
        @Mapping(source = "player1Id", target = "player1.id")
        @Mapping(source = "player2Id", target = "player2.id")
        @Mapping(source = "matchOthersInfoDTO.time", target = "time")
        @Mapping(source = "matchOthersInfoDTO.movingTime", target = "movingTime")
        @Mapping(source = "matchOthersInfoDTO.cumulativeTime", target = "cumulativeTime")
        @Mapping(source = "matchOthersInfoDTO.eloBet", target = "eloBet")
        @Mapping(ignore = true, target = "result")
        @Mapping(ignore = true, target = "createdDate")
        @Mapping(ignore = true, target = "createdByUserId")
        @Mapping(ignore = true, target = "lastModifiedDate")
        @Mapping(ignore = true, target = "lastModifiedByUserId")
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
