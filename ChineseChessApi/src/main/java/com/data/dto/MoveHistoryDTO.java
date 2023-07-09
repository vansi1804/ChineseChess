package com.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MoveHistoryDTO extends MovedResponseDTO{
    
    private long turn;

    private String description;

    private PlayBoardDTO currentBoardDTO;

    private List<PieceDTO> lastDeadPieceDTOs;

}
