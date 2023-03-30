package com.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {   // show for user
    private String phoneNumber;
    private String name;
    private String avatar;
    private String vipName;
}
