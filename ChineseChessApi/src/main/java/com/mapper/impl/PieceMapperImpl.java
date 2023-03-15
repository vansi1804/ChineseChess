package com.mapper.impl;

import org.springframework.stereotype.Component;

import com.dto.PieceDTO;
import com.entity.Piece;
import com.mapper.PieceMapper;

@Component
public class PieceMapperImpl implements PieceMapper {

    @Override
    public Piece toEntity(PieceDTO pieceDTO) {
        if (pieceDTO == null)
            return null;
        Piece piece = new Piece();
        piece.setId(pieceDTO.getId());
        piece.setName(pieceDTO.getName());
        piece.setRed(pieceDTO.isRed());
        piece.setImage(pieceDTO.getImage());
        piece.setStartCol(pieceDTO.getStartCol());
        piece.setStartRow(pieceDTO.getStartRow());
        return piece;
    }

    @Override
    public PieceDTO toDTO(Piece piece) {
        if (piece == null)
            return null;
        PieceDTO pieceDTO = new PieceDTO();
        pieceDTO.setId(piece.getId());
        pieceDTO.setName(piece.getName());
        pieceDTO.setRed(piece.isRed());
        pieceDTO.setImage(piece.getImage());
        pieceDTO.setStartCol(piece.getStartCol());
        pieceDTO.setStartRow(piece.getStartRow());
        return pieceDTO;
    }

}
