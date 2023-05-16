package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveHistoryCreationDTO {
    private long matchId;
    private int pieceId;
    private int fromCol;
    private int fromRow;
    private int toCol;
    private int toRow;
    List<PieceDTO> deadPieceDTOs;
}
