package com.data.dto.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerProfileDTO { 
    private long id;
    private UserProfileDTO userProfileDTO;
    private String levelsName;
    private int elo;
}