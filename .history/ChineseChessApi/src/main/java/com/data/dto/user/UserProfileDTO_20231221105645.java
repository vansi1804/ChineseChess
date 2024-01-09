package com.data.dto.user;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;
import com.data.dto.VipDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProfileDTO {  
    
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    private String avatar;

    private VipDTO vipDTO;

}
