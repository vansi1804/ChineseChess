package com.service.impl;

import org.springframework.stereotype.Service;


import com.common.enumeration.EIndexDescription;
import com.common.enumeration.EMoveTypeDescription;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.service.MoveDescriptionService;
import com.service.MoveTypeService;
import com.service.PieceService;
import com.common.Default;

@Service
public class MoveDescriptionServiceImpl implements MoveDescriptionService {
    
    private final int MAX_COL = Default.Game.PlayBoardSize.COL_MAX;

    private final PieceService pieceService;
    private final MoveTypeService moveTypeService;

    public MoveDescriptionServiceImpl(
        PieceService pieceService, 
        MoveTypeService moveTypeService) {

        this.pieceService = pieceService;
        this.moveTypeService = moveTypeService;
    }

    @Override
    public String build(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        String shortNameOfPiece = pieceService.convertByName(pieceDTO.getName()).getShortName();

        StringBuilder description = new StringBuilder();
        description.append(shortNameOfPiece);

        if (pieceDTO.isRed()) {
            description.append(this.buildRedPieceDescription(playBoardDTO, pieceDTO, toCol + 1, toRow + 1));
        } else {
            description.append(this.buildBlackPieceDescription(playBoardDTO, pieceDTO, toCol + 1, toRow + 1));
        }

        return description.toString();
    }

    private String buildRedPieceDescription(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        StringBuilder description = new StringBuilder();
        description.append(buildIndexDescription(playBoardDTO, pieceDTO));
        description.append(MAX_COL - pieceDTO.getCurrentCol());

        if (moveTypeService.isHorizontallyMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMoveTypeDescription.ACROSS.getValue())
                    .append(MAX_COL - toCol);

        } else {
            if (moveTypeService.isUpMoving(true, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMoveTypeDescription.UP.getValue());
            } else {
                description.append(EMoveTypeDescription.DOWN.getValue());
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
        description.append(pieceDTO.getCurrentCol());

        if (moveTypeService.isHorizontallyMoving(pieceDTO.getCurrentRow(), toRow)) {
            description.append(EMoveTypeDescription.ACROSS.getValue())
                    .append(toCol);
                    
        } else {
            if (moveTypeService.isUpMoving(false, pieceDTO.getCurrentRow(), toRow)) {
                description.append(EMoveTypeDescription.UP.getValue());
            } else {
                description.append(EMoveTypeDescription.DOWN.getValue());
            }

            if (moveTypeService.isVerticallyMoving(pieceDTO.getCurrentCol(), toCol)) {
                description.append(Math.abs(pieceDTO.getCurrentRow() - toRow));
            } else {
                description.append(toCol);
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
                                ? EIndexDescription.BEFORE.getValue()
                                : EIndexDescription.AFTER.getValue();
    }

}
