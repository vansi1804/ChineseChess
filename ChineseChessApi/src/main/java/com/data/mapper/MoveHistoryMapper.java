package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.MatchDetailDTO;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.entity.MoveHistory;

@Mapper(componentModel = "spring", uses = { MatchMapper.class})
public interface MoveHistoryMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(ignore = true, target = "turn")
    @Mapping(source = "pieceId", target = "piece.id")
    MoveHistory toEntity(MoveHistoryCreationDTO moveHistoryCreationDTO);

    @Mapping(source = "match", target = "matchDTO")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "deadPieceDTOs")
    @Mapping(ignore = true, target = "playBoard")
    MatchDetailDTO toDTO(MoveHistory moveHistory);
}
