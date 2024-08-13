package com.nvs.data.dto.match;

import com.nvs.data.dto.AuditorDTO;
import com.nvs.data.dto.player.PlayerProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MatchDTO extends AuditorDTO {

  private long id;

  private PlayerProfileDTO player1ProfileDTO;

  private PlayerProfileDTO player2ProfileDTO;

  private MatchOthersInfoDTO matchOthersInfoDTO;

  private Integer result;

}
