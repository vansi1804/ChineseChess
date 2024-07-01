package com.nvs.data.dto.move.availableMove.bestAvailableMove;

import com.nvs.data.dto.PieceDTO;
import java.util.List;
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
