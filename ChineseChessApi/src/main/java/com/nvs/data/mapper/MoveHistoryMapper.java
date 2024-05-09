package com.nvs.data.mapper;

import com.nvs.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.nvs.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.nvs.data.entity.MoveHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoveHistoryMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(ignore = true, target = "training")
    @Mapping(ignore = true, target = "turn")
    @Mapping(source = "movingPieceId", target = "piece.id")
    MoveHistory toEntity(MatchMoveCreationDTO matchMoveCreationDTO);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "match")
    @Mapping(source = "trainingId", target = "training.id")
    @Mapping(ignore = true, target = "turn")
    @Mapping(source = "movingPieceId", target = "piece.id")
    MoveHistory toEntity(TrainingMoveCreationDTO trainingMoveCreationDTO);
}
