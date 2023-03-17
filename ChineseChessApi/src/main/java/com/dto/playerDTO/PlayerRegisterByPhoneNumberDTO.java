package com.dto.playerDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.common.ErrorMessage;
import com.common.ValidationREGEX;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerRegisterByPhoneNumberDTO {
    @Pattern(regexp = ValidationREGEX.PHONE_NUMBER_REGEX)
    private String phoneNumber;
    @NotBlank(message = ErrorMessage.BLANK_PASSWORD)
    @Size(min = 8,message = ErrorMessage.SIZE_PASSWORD)
    private String password;
}
