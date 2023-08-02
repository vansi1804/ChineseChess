package com.data.dto.move;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.common.Validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDetailCreationDTO implements Serializable {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer movingPieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.AREA_MIN -1, message = ErrorMessage.COL)
    @Max(value = Validation.COL_MAX - 1, message = ErrorMessage.COL)
    private Integer toCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.AREA_MIN -1, message = ErrorMessage.ROW)
    @Max(value = Validation.ROW_MAX - 1, message = ErrorMessage.ROW)
    private Integer toRow;

}
