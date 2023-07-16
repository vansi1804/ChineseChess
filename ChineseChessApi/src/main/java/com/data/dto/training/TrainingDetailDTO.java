package com.data.dto.training;

import java.util.Map;

import com.data.dto.move.MoveHistoryDTO;

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
