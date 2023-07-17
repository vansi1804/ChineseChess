package com.data.dto.move;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDetailDTO implements Serializable{
    
    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer pieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer toCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer toRow;

}
