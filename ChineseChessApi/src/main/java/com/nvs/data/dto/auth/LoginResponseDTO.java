package com.nvs.data.dto.auth;

import com.nvs.data.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LoginResponseDTO extends JwtResponseDTO {

  private long userId;

  private RoleDTO roleDTO;
}
