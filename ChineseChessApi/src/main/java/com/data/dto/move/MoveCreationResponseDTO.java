package com.data.dto.move;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveCreationResponseDTO{
    
    private PieceDTO movingPieceDTO;

    private int toCol;

    private int toRow;

    private PlayBoardDTO currentBoardDTO;

    private PieceDTO deadPieceDTO;

    private PieceDTO checkedGeneralPieceDTO;
    
    private boolean isCheckMate;

}
