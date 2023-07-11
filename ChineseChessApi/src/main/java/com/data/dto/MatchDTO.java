package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MatchDTO extends AuditingDTO{

    private long id;

    private Integer time;

    private Integer movingTime;

    private Integer cumulativeTime;

    private Integer bet;

    private PlayerProfileDTO player1ProfileDTO;

    private PlayerProfileDTO player2ProfileDTO;

    private Long result;
    
}
