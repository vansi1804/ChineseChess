package com.data.dto.match;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO implements Serializable{

    @Min(value = 5, message = " > 5 (minutes)")
    private Integer time;

    @Min(value = 30, message = " > 30 (seconds)")
    private Integer movingTime;

    @Min(value = 3, message = " > 3 (seconds)")
    private Integer cumulativeTime;

    @Min(value = 100, message = " > 100 (elo)")
    private Integer eloBet;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long player1Id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long player2Id;

}
