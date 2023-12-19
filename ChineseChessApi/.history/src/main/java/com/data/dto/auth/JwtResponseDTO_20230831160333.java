package com.data.dto.auth;

import java.io.Serializable;

import com.common.Default;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JwtResponseDTO implements Serializable {

    private final String tokenType = Default.JWT.TOKEN_PREFIX.trim();

    private String accessToken;

    private String refreshToken;

}
