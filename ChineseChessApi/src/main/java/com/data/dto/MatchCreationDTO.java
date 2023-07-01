package com.data.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO {

    @Min(value = 5, message = "time >= 5 minutes")
    private int time;

    @Min(value = 15, message = "movingTime >= 15 seconds")
    private int movingTime;

    @Min(value = 1, message = "cumulativeTime > seconds")
    private int cumulativeTime;

    @Min(value = 1, message = "bet > 0")
    private int bet;

    private long player1Id;

    private long player2Id;
    
}
