package com.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.data.dto.MatchMoveCreationDTO;
import com.data.dto.TrainingMoveCreationDTO;
import com.data.entity.MoveHistory;

@Mapper(componentModel = "spring")
public interface MoveHistoryMapper {
    
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(ignore = true, target = "training")
    @Mapping(ignore = true, target = "turn")
    @Mapping(source = "pieceId", target = "piece.id")
    MoveHistory toEntity(MatchMoveCreationDTO moveHistoryCreationDTO);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "match")
    @Mapping(source = "trainingId", target = "training.id")
    @Mapping(ignore = true, target = "turn")
    @Mapping(source = "pieceId", target = "piece.id")
    MoveHistory toEntity(TrainingMoveCreationDTO TrainingMoveHistoryCreationDTO);

}
