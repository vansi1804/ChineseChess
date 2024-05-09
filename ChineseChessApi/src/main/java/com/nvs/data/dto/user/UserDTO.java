package com.nvs.data.dto.user;

import com.nvs.data.dto.AuditorDTO;
import com.nvs.data.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDTO extends AuditorDTO {

  private long id;

  private UserProfileDTO userProfileDTO;

  private RoleDTO roleDTO;

  private String status;
}
