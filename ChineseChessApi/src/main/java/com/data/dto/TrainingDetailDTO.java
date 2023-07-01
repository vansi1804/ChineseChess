package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingDetailDTO {
    private TrainingDTO trainingDTO;
    private long totalTurn;
    private List<MoveHistoryDTO> moveHistoryDTOs;
}
