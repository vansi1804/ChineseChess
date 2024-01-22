package com.data.dto.match;

import com.common.Default;
import com.common.ErrorMessage;
import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchOthersInfoDTO implements Serializable {

  @Min(value = Default.Game.MATCH_TIME, message = ErrorMessage.MATCH_TIME)
  private Integer time;

  @Min(value = Default.Game.MOVING_TIME, message = ErrorMessage.MOVING_TIME)
  private Integer movingTime;

  @Min(
    value = Default.Game.CUMULATIVE_TIME,
    message = ErrorMessage.CUMULATIVE_TIME
  )
  private Integer cumulativeTime;

  @Min(value = Default.Game.BET_ELO, message = ErrorMessage.BET_ELO)
  private Integer eloBet;
}
