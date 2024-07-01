package com.nvs.data.dto;

import com.nvs.common.Default;
import com.nvs.common.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
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
