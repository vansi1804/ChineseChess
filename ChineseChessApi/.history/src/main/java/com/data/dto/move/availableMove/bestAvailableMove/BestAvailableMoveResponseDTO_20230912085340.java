package com.data.dto.move.availableMove.bestAvailableMove;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestAvailableMoveResponseDTO {

    private int evalScore;

    private List<BestMoveResponseDTO> bestMovesResponseDTOs;

}
