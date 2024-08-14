package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.config.exception.UnauthorizedExceptionCustomize;
import com.nvs.config.security.jwt.JwtService;
import com.nvs.config.security.userDetails.UserDetailsImpl;
import com.nvs.data.dto.auth.LoginDTO;
import com.nvs.data.dto.auth.LoginResponseDTO;
import com.nvs.data.mapper.RoleMapper;
import com.nvs.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RoleMapper roleMapper;

  @Override
  public LoginResponseDTO login(LoginDTO loginDTO) {
    log.info("Attempting login for phone number: {}", loginDTO.getPhoneNumber());

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDTO.getPhoneNumber(), loginDTO.getPassword())
      );

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      log.info("Authentication successful for phone number: {}", loginDTO.getPhoneNumber());

      LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

      if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
        log.debug("Fetching user details for user ID: {}", userDetailsImpl.getUser().getId());
        loginResponseDTO.setUserId(userDetailsImpl.getUser().getId());
        loginResponseDTO.setRoleDTO(roleMapper.toDTO(userDetailsImpl.getUser().getRole()));
      }

      String accessToken = jwtService.generateToken(userDetails.getUsername(),
          Default.JWT.ACCESS_TOKEN_EXPIRATION_TIME);
      String refreshToken = jwtService.generateToken(userDetails.getUsername(),
          Default.JWT.REFRESH_EXPIRATION_TIME);

      loginResponseDTO.setAccessToken(accessToken);
      loginResponseDTO.setRefreshToken(refreshToken);

      log.info("Generated JWT tokens for phone number: {}", loginDTO.getPhoneNumber());

      return loginResponseDTO;

    } catch (BadCredentialsException ex) {
      log.warn("Login failed for phone number: {}", loginDTO.getPhoneNumber());
      throw new UnauthorizedExceptionCustomize();
    }
  }
}
