package com.nvs.data.dto.match;

import com.nvs.data.dto.move.MoveHistoryDTO;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchDetailDTO {

  private MatchDTO matchDTO;

  private Long totalTurn;

  private Map<Long, MoveHistoryDTO> moveHistoryDTOs;

}
