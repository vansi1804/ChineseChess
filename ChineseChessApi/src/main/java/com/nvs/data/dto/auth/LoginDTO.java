package com.nvs.data.dto.auth;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import com.nvs.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO implements Serializable {

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String password;
}
