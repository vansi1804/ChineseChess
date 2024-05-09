package com.nvs.data.dto.auth;

import com.nvs.common.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JwtResponseDTO implements Serializable {

  private final String tokenType = Default.JWT.TOKEN_PREFIX.trim();

  private String accessToken;

  private String refreshToken;
}