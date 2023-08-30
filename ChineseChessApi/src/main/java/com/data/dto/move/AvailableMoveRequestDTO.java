package com.data.dto.move;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.config.validation.Validator;
import com.config.validation.impl.PlayBoardValidator;
import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvailableMoveRequestDTO {
    
    @NotNull(message = ErrorMessage.BLANK_DATA)
    private Integer movingPieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    @Validator(PlayBoardValidator.class)
    private PlayBoardDTO playBoardDTO;
    
}
