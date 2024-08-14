package com.nvs.data.dto.match;

import com.nvs.common.Default;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchOthersInfoDTO implements Serializable {

  @NotNull(message = "NULL_DATA")
  @Min(value = Default.Game.MATCH_TIME, message = "MATCH_TIME")
  private Integer time;

  @NotNull(message = "NULL_DATA")
  @Min(value = Default.Game.MOVING_TIME, message = "MOVING_TIME")
  private Integer movingTime;

  @NotNull(message = "NULL_DATA")
  @Min(value = Default.Game.CUMULATIVE_TIME, message = "CUMULATIVE_TIME")
  private Integer cumulativeTime;

  @NotNull(message = "NULL_DATA")
  @Min(value = Default.Game.BET_ELO, message = "BET_ELO")
  private Integer eloBet;

}
