package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveHistoryDTO {
    
    private long turn;

    private PieceDTO movingPieceDTO;

    private String description;

    private PlayBoardDTO currentBoard;

    private PieceDTO generalBeingChecked;

    private PieceDTO deadPieceDTO;

}
