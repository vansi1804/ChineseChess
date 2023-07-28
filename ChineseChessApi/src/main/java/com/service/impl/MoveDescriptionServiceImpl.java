package com.service.impl;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.common.enumeration.EIndex;
import com.common.enumeration.EMoveType;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveDescriptionService;
import com.service.MoveTypeService;
import com.service.PieceService;
import com.common.Validation;

@Service
public class MoveDescriptionServiceImpl implements MoveDescriptionService {
    
    private final int MAX_COL = Validation.COL_MAX;

    private final PieceService pieceService;
    private final MoveTypeService moveTypeService;

    @Autowired
    public MoveDescriptionServiceImpl(PieceService pieceService, MoveTypeService moveTypeService) {
        this.pieceService = pieceService;
        this.moveTypeService = moveTypeService;
    }

    @Override
    public String build(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        String shortNameOfPiece = pieceService.convertByName(pieceDTO.getName()).getShortName();

        StringBuilder description = new StringBuilder();
        description.append(shortNameOfPiece);

        if (pieceDTO.isRed()) {
            description.append(buildRedPieceDescription(playBoardDTO, pieceDTO, toCol, toRow));
        } else {
            description.append(buildBlackPieceDescription(playBoardDTO, pieceDTO, toCol, toRow));
        }

        return description.toString();
    }

    private String buildRedPieceDescription(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(buildIndexDescription(playBoardDTO, pieceDTO));
        description.append(MAX_COL - pieceDTO.getCurrentCol());

        if (moveTypeService.isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMoveType.ACROSS.getValue())
                    .append(MAX_COL - toCol);
        } else {
            if (moveTypeService.isUpMoving(true, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMoveType.UP.getValue());
            } else {
                description.append(EMoveType.DOWN.getValue());
            }

            if (moveTypeService.isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(Math.abs(pieceDTO.getCurrentRow() - toRow));
            } else {
                description.append(MAX_COL - toCol);
            }
        }

        return description.toString();
    }

    private String buildBlackPieceDescription(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(buildIndexDescription(playBoardDTO, pieceDTO));
        description.append(pieceDTO.getCurrentCol() + 1);

        if (moveTypeService.isHorizontalMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMoveType.ACROSS.getValue())
                    .append(toCol + 1);
        } else {
            if (moveTypeService.isUpMoving(false, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMoveType.UP.getValue());
            } else {
                description.append(EMoveType.DOWN.getValue());
            }

            if (moveTypeService.isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(Math.abs(pieceDTO.getCurrentRow() - toRow));
            } else {
                description.append(toCol + 1);
            }
        }

        return description.toString();
    }

    private String buildIndexDescription(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        PieceDTO foundPiece = pieceService.findExistingTheSameInColPath(playBoardDTO, pieceDTO);
        return foundPiece == null
                ? ""
                : (pieceDTO.isRed() && (pieceDTO.getCurrentRow() < foundPiece.getCurrentRow()))
                        || (!pieceDTO.isRed() && (pieceDTO.getCurrentRow() > foundPiece.getCurrentRow()))
                                ? EIndex.BEFORE.getValue()
                                : EIndex.AFTER.getValue();
    }

}
