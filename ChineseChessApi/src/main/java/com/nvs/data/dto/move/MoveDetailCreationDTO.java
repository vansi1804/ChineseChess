package com.nvs.data.dto.move;

import com.nvs.common.Default;
import com.nvs.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDetailCreationDTO implements Serializable {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer movingPieceId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.PlayBoardSize.COL_MIN, message = ErrorMessage.COL)
    @Max(value = Default.Game.PlayBoardSize.COL_MAX, message = ErrorMessage.COL)
    private Integer toCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.PlayBoardSize.ROW_MIN, message = ErrorMessage.ROW)
    @Max(value = Default.Game.PlayBoardSize.ROW_MAX, message = ErrorMessage.ROW)
    private Integer toRow;
}
