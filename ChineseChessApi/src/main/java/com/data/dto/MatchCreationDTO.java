package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO {
    private int time;
    private int movingTime;
    private int cumulativeTime;
    private int bet;
    private long player1Id;
    private long player2Id;
}
