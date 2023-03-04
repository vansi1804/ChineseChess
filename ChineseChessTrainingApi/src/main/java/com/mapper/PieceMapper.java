package com.mapper;

import org.mapstruct.Mapper;

import com.dto.PieceDTO;
import com.entity.Piece;

@Mapper(componentModel = "spring")
public interface PieceMapper {
    Piece toEntity(PieceDTO pieceDTO);

    PieceDTO toDTO(Piece piece);
}
