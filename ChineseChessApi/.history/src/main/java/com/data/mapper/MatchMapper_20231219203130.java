package com.data.mapper;

import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.match.MatchDetailDTO;
import com.data.dto.match.MatchOthersInfoDTO;
import com.data.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface MatchMapper {
  @Mappings(
    {
      @Mapping(ignore = true, target = "id"),
      @Mapping(ignore = true, target = "result"),
      @Mapping(ignore = true, target = "createdDate"),
      @Mapping(ignore = true, target = "createdByUserId"),
      @Mapping(ignore = true, target = "lastModifiedDate"),
      @Mapping(ignore = true, target = "lastModifiedByUserId"),
      @Mapping(source = "player1", target = "player1ProfileDTO"),
      @Mapping(source = "player1", target = "player1ProfileDTO"),
      @Mapping(source = "player1", target = "player1ProfileDTO"),
      @Mapping(source = "player1", target = "player1ProfileDTO"),
    }
  )
  Match toEntity(MatchCreationDTO matchCreationDTO);

  @Mappings(
    {
      @Mapping(source = "player1", target = "player1ProfileDTO"),
      @Mapping(source = "player2", target = "player2ProfileDTO"),
      @Mapping(
        expression = "java(toOthersInfoDTO(match))",
        target = "matchOthersInfoDTO"
      ),
    }
  )
  MatchDTO toDTO(Match match);

  @Mappings(
    {
      @Mapping(expression = "java(toDTO(match))", target = "matchDTO"),
      @Mapping(ignore = true, target = "moveHistoryDTOs"),
      @Mapping(ignore = true, target = "totalTurn"),
    }
  )
  MatchDetailDTO toDetailDTO(Match match);

  MatchOthersInfoDTO toOthersInfoDTO(Match match);
}
