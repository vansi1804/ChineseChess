package com.data.dto;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerProfileDTO { 
    
    private Long id;

    @Valid
    private UserProfileDTO userProfileDTO;

    private String rankName;

    private int elo;

    private int win;

    private int draw;

    private int lose;

}
