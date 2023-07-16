package com.data.dto.player;

import javax.validation.Valid;

import com.data.dto.user.UserProfileDTO;

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
