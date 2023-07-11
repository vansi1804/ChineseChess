package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MovedResponseDTO{
    
    private PieceDTO movedPieceDTO;

    private PlayBoardDTO currentBoardDTO;

    private PieceDTO deadPieceDTO;

    private PieceDTO checkedGeneralPieceDTO;
    
    private boolean isCheckMate;

}
