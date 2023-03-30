package com.data.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchDTO {
    private int time;
    private int movingTime;
    private int cumulativeTime;
    private int bet;
    private long player1Id;
    private String player1Name;
    private String player1Avatar;
    private long player2Id;
    private String player2Name;
    private String player2Avatar;
    private String result;
    private LocalDateTime startAt;
}
