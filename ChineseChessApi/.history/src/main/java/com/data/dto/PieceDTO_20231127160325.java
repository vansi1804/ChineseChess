package com.data.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.common.Default;
import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO implements Serializable {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private String name;

    private boolean isRed;

    private String image;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.PlayBoardSize.COL_MIN, message = ErrorMessage.COL)
    @Max(value = Default.Game.PlayBoardSize.COL_MAX, message = ErrorMessage.COL)
    private Integer currentCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.PlayBoardSize.ROW_MIN, message = ErrorMessage.ROW)
    @Max(value = Default.Game.PlayBoardSize.ROW_MAX, message = ErrorMessage.ROW)
    private Integer currentRow;

}
