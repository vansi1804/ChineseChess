package com.data.dto.move;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDTO {
    
    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer pieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer toCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer toRow;

}
