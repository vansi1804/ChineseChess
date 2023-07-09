package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MovedResponseDTO {
    
    private PieceDTO deadPieceDTO;

    private PieceDTO generalBeingChecked;

    private boolean isCheckMate; 

}
