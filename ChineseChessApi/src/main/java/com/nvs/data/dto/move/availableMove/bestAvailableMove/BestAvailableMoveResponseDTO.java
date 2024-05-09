package com.nvs.data.dto.move.availableMove.bestAvailableMove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestAvailableMoveResponseDTO {

  private int evalScore;

  private List<BestMoveResponseDTO> bestMovesResponseDTOs;
}
