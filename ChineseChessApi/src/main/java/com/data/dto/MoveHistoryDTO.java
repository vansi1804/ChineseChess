package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveHistoryDTO {
    private long turn;
    private String description;
    private List<PieceDTO> deadPieceDTOs;
    private PlayBoardDTO playBoard;
}
