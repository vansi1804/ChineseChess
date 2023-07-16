package com.data.dto.user;

import com.data.dto.AuditingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDTO extends AuditingDTO {    
    
    private long id;

    private UserProfileDTO userProfileDTO;

    private String roleName;

    private String status;
    
}
