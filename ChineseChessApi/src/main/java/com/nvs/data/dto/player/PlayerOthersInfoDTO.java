package com.nvs.data.dto.player;

import com.nvs.data.dto.RankDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerOthersInfoDTO{

   private int elo;

   private RankDTO rankDTO;

   private int win;

   private int draw;

   private int lose;

}
