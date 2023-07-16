package com.data.dto.move;

import java.util.List;

import com.data.dto.PieceDTO;

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

    private List<PieceDTO> lastDeadPieceDTOs;

}
