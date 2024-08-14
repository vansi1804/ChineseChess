package com.nvs.data.dto.move;

import com.nvs.common.Default;
import com.nvs.common.Default.Game.PlayBoardSize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDetailCreationDTO implements Serializable {

  @NotNull(message = "NULL_DATA")
  private Integer movingPieceId;

  @NotNull(message = "NULL_DATA")
  @Size(max = Default.Game.PlayBoardSize.COL_MAX, message = "COL")
  private Integer toCol;

  @NotNull(message = "NULL_DATA")
  @Size(max = PlayBoardSize.ROW_MAX, message = "ROW")
  private Integer toRow;

}
