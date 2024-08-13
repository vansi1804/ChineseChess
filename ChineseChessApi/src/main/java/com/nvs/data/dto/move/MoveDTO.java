package com.nvs.data.dto.move;

import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MoveDTO {

  private PieceDTO movingPieceDTO;

  private int toCol;

  private int toRow;

  private PieceDTO deadPieceDTO;

  private PlayBoardDTO playBoardDTO;

  private PieceDTO checkedGeneralPieceDTO;

  private boolean isCheckmateState;

}
