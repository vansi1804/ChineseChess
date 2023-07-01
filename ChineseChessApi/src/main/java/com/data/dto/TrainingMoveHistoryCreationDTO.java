package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingMoveHistoryCreationDTO {
    private long trainingId;
    private int pieceId;
    private int toCol;
    private int toRow;
}
