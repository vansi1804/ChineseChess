package com.nvs.data.dto.training;

import com.nvs.data.dto.move.MoveHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingDetailDTO {

    private TrainingDTO trainingDTO;

    private long totalTurn;

    private Map<Long, MoveHistoryDTO> moveHistoryDTOs;
}
