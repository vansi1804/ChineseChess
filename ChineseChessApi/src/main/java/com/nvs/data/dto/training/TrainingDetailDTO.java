package com.nvs.data.dto.training;

import com.nvs.data.dto.move.MoveHistoryDTO;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingDetailDTO{

   private TrainingDTO trainingDTO;

   private long totalTurn;

   private Map<Long, MoveHistoryDTO> moveHistoryDTOs;

}
