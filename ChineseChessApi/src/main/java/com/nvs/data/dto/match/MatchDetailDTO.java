package com.nvs.data.dto.match;

import com.nvs.data.dto.match.MatchDTO;
import com.nvs.data.dto.move.MoveHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchDetailDTO {

  private MatchDTO matchDTO;

  private Long totalTurn;

  private Map<Long, MoveHistoryDTO> moveHistoryDTOs;
}
