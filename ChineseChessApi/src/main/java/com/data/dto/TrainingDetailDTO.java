package com.data.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingDetailDTO {
    
    private TrainingDTO trainingDTO;

    private long totalTurn;

    private Map<Long, MoveHistoryDTO> moveHistoryDTOs;

}
