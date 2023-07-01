package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ValidMoveRequestDTO {
    private PlayBoardDTO playBoardDTO;
    private int pieceId;
}
