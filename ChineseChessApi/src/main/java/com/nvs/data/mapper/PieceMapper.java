package com.nvs.data.mapper;

import com.nvs.data.dto.PieceDTO;
import com.nvs.data.entity.Piece;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PieceMapper {
    Piece toEntity(PieceDTO pieceDTO);

    Piece[] toEntity(PieceDTO[] pieceDTOs);

    Piece[][] toEntity(PieceDTO[][] pieceDTOs);

    PieceDTO toDTO(Piece piece);

    PieceDTO[] toDTO(Piece[] pieceDTOs);

    PieceDTO[][] toDTO(Piece[][] Pieces);

    PieceDTO copy(PieceDTO pieceDTO);

    PieceDTO[] copy(PieceDTO[] pieceDTO);

    PieceDTO[][] copy(PieceDTO[][] pieceDTO);
}
