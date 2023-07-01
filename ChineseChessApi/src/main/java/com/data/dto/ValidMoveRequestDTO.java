package com.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ValidMoveRequestDTO {
    
    private int pieceId;

    @NotNull(message = ErrorMessage.BLANK_DATA)
    @Valid
    private PlayBoardDTO currentBoard;
    
}
