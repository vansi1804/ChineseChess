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
    
    @NotNull(message = ErrorMessage.BLANK_DATA)
    private Integer pieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private PlayBoardDTO currentBoardDTO;
    
}
