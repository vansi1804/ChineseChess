package com.mapper.impl;

import org.springframework.stereotype.Component;

import com.dto.PieceDTO;
import com.entity.Piece;
import com.mapper.PieceMapper;

@Component
public class PieceMapperImpl implements PieceMapper {

    @Override
    public Piece toEntity(PieceDTO pieceDTO) {
        return new Piece(
                pieceDTO.getId(),
                pieceDTO.getName(),
                pieceDTO.isRed(),
                pieceDTO.getImage(),
                pieceDTO.getStartCol(),
                pieceDTO.getStartRow());
    }

    @Override
    public PieceDTO toDTO(Piece piece) {
        return new PieceDTO(
                piece.getId(),
                piece.getName(),
                piece.isRed(),
                piece.getImage(),
                piece.getStartCol(),
                piece.getStartRow());
    }

}
