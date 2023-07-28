package com.data.dto.match;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.common.Validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO implements Serializable{

    @Min(value = Validation.MATCH_TIME, message = ErrorMessage.MATCH_TIME)
    private Integer time;

    @Min(value = Validation.MOVING_TIME, message = ErrorMessage.MOVING_TIME)
    private Integer movingTime;

    @Min(value = Validation.CUMULATIVE_TIME, message = ErrorMessage.CUMULATIVE_TIME)
    private Integer cumulativeTime;

    @Min(value = Validation.BET_ELO, message = ErrorMessage.BET_ELO)
    private Integer eloBet;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long player1Id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long player2Id;

}
