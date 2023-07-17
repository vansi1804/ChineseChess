package com.data.dto.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProfileDTO implements Serializable{  
    
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    private String avatar;

    private String vipName;

}
