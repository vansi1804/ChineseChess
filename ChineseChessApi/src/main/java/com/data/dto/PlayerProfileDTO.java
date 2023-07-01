package com.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerProfileDTO { 
    
    private long id;

    private UserProfileDTO userProfileDTO;

    private String rankName;

    private int elo;

    private int win;

    private int lost;

    private int draw;

}
