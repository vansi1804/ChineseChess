package com.nvs.data.dto.move.availableMove.bestAvailableMove;

import com.nvs.data.dto.PieceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestMoveResponseDTO {

  private PieceDTO pieceDTO;

  private List<int[]> bestAvailableMoveIndexes;
}
