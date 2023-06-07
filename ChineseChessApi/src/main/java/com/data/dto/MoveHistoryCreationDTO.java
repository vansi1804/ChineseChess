package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveHistoryCreationDTO {
    private long matchId;
    private long playerId;
    private int pieceId;
    private int toCol;
    private int toRow;
}
