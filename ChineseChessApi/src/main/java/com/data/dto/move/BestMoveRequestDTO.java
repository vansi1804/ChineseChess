package com.data.dto.move;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestMoveRequestDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private PlayBoardDTO playBoardDTO;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Boolean isRed;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer depth;

}
