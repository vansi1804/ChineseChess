package com.data.dto;

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
public class PieceDTO implements Serializable{

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private String name;

    private boolean isRed;

    private String image;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.AREA_MIN - 1, message = ErrorMessage.COL)
    @Max(value = Validation.COL_MAX - 1, message = ErrorMessage.COL)
    private Integer currentCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.AREA_MIN - 1, message = ErrorMessage.ROW)
    @Max(value = Validation.ROW_MAX - 1, message = ErrorMessage.ROW)
    private Integer currentRow;

}
