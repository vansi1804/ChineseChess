package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchStartDTO {
    private MatchDTO matchDTO;
    private PlayBoardDTO playBoardStartDTO;
}
