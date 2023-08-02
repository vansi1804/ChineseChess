package com.data.dto.move;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.config.validation.Validator;
import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvailableMoveRequest {
    
    @NotNull(message = ErrorMessage.BLANK_DATA)
    private Integer movingPieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    @Validator
    private PlayBoardDTO playBoardDTO;
    
}
