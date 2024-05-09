package com.nvs.data.dto.match;

import com.nvs.common.Default;
import com.nvs.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchOthersInfoDTO implements Serializable {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.MATCH_TIME, message = ErrorMessage.MATCH_TIME)
    private Integer time;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.MOVING_TIME, message = ErrorMessage.MOVING_TIME)
    private Integer movingTime;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.CUMULATIVE_TIME, message = ErrorMessage.CUMULATIVE_TIME)
    private Integer cumulativeTime;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Default.Game.BET_ELO, message = ErrorMessage.BET_ELO)
    private Integer eloBet;
}
