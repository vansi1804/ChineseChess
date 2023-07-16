package com.data.dto;

import com.common.Default;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JwtResponseDTO {

    private final String tokenType = Default.JWT.TOKEN_PREFIX.trim();

    private String accessToken;

    private String refeshToken;

}
