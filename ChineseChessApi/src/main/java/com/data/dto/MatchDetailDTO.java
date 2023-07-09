package com.data.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchDetailDTO {

    private MatchDTO matchDTO;

    private long totalTurn;

    private Map<Long, MoveHistoryDTO> moveHistoryDTOs;

}
