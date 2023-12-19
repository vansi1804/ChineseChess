package com.data.dto.match;

import java.util.Map;

import com.data.dto.move.MoveHistoryDTO;

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
