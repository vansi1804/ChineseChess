package com.data.dto.move.availableMove.bestAvailableMove;

import java.util.List;

import com.data.dto.PieceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestMoveResponseDTO {

    private PieceDTO pieceDTO;

    private List<int[]> bestAvailableMoveIndexes;

}
