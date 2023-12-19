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

    private long win;

    private long draw;

    private long lose;
    
}
