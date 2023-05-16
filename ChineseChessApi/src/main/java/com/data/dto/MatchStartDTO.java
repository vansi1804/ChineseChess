package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchStartDTO {
    private MatchDTO matchDTO;
    private List<PieceDTO> deadPieceDTOs;
    private PlayBoardDTO playBoardStartDTO;
}
