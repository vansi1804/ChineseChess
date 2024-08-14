package com.nvs.data.dto;

import com.nvs.common.Default;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO implements Serializable {

  @NotNull(message = "BLANK_DATA")
  private Integer id;

  @NotNull(message = "BLANK_DATA")
  private String name;

  private boolean isRed;

  private String image;

  @NotNull(message = "NULL_DATA")
  @Size(max = Default.Game.PlayBoardSize.COL_MAX, message = "COL")
  private Integer currentCol;

  @NotBlank(message = "NOT_BLANK")
  @Size(max = Default.Game.PlayBoardSize.ROW_MAX, message = "ROW")
  private Integer currentRow;

}
