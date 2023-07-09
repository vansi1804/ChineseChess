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

    private Integer time;

    private Integer movingTime;

    private Integer cumulativeTime;

    private Integer bet;

    private PlayerProfileDTO player1ProfileDTO;

    private PlayerProfileDTO player2ProfileDTO;

    private Long result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date stopAt;
    
}
