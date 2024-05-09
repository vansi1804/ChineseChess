package com.nvs.data.dto.move;

import com.nvs.data.dto.move.MoveDTO;
import com.nvs.data.dto.PieceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MoveHistoryDTO extends MoveDTO {

  private long turn;

  private String description;

  private List<PieceDTO> lastDeadPieceDTOs;
}
