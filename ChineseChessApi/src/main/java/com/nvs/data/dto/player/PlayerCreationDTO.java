package com.nvs.data.dto.player;

import com.nvs.common.ErrorMessage;
import com.nvs.data.dto.user.UserCreationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerCreationDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private UserCreationDTO userCreationDTO;
}
