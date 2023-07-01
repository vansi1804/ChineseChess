package com.data.dto;

import java.util.Date;

import com.common.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchDTO {

    private long id;

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

    private Long result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date stopAt;
    
}
