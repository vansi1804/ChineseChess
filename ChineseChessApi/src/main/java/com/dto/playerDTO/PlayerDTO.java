package com.dto.playerDTO;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;
import com.dto.LevelsDTO;
import com.dto.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerDTO{
    private String email;
    private String phoneNumber;
    @NotBlank(message = ErrorMessage.BLANK_NAME)
    private String name;
    private String avatar;
    private long eloScore;
    private LevelsDTO levelsDTO;
    private RoleDTO roleDTO;
}
