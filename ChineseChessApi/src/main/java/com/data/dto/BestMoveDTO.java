package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestMoveDTO {

    private  PieceDTO pieceDTO;

    private List<int[]> bestMoves;
    
}
