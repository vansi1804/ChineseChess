package com.data.dto.register;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.common.ErrorMessage;
import com.common.RegexValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserRegisterDTO {
    @Pattern(regexp = RegexValidation.PHONE_NUMBER_REGEX)
    private String phoneNumber;
    @Size(min = RegexValidation.PASSWORD_SIZE, message = ErrorMessage.SIZE_PASSWORD)
    private String password;
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;
    private String avatar;
}
