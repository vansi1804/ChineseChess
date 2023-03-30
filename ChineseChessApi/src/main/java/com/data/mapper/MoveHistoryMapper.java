package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.GameViewDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.entity.MoveHistory;

@Mapper(componentModel = "spring", uses = { MatchMapper.class})
public interface MoveHistoryMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "match")
    @Mapping(ignore = true, target = "turn")
    @Mapping(ignore = true, target = "piece")
    MoveHistory toEntity(MoveHistoryDTO moveHistoryDTO);

    @Mapping(source = "match", target = "matchDTO")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "deadPieceDTOs")
    @Mapping(ignore = true, target = "playBoard")
    GameViewDTO toDTO(MoveHistory moveHistory);
}
