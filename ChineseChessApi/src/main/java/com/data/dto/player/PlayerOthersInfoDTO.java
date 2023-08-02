package com.data.dto.player;

import com.data.dto.RankDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerOthersInfoDTO {

    private RankDTO rankDTO;

    private int elo;

    private int win;

    private int draw;

    private int lose;
    
}
