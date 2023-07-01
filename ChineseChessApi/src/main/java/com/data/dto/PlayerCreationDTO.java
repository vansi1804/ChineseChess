package com.data.dto;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCreationDTO {

    @NotNull(message = "UserCreationDTO must not be null")
    @Valid
    private UserCreationDTO userCreationDTO;
    
}
