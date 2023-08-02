package com.data.dto.player;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.data.dto.user.UserProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerProfileDTO { 
    
    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private UserProfileDTO userProfileDTO;

    private PlayerOthersInfoDTO playerOthersInfoDTO;

}
