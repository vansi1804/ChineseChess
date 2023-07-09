package com.data.dto;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerProfileDTO { 
    
    private long id;

    @Valid
    private UserProfileDTO userProfileDTO;

    private String rankName;

    private int elo;

    private int win;

    private int draw;

    private int lose;

}
