package com.service.impl;

import com.common.Default;
import com.config.exception.UnauthorizedExceptionCustomize;
import com.config.security.jwt.JwtService;
import com.config.security.userDetails.UserDetailsImpl;
import com.data.dto.auth.LoginDTO;
import com.data.dto.auth.LoginResponseDTO;
import com.data.mapper.RoleMapper;
import com.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RoleMapper roleMapper;

  @Override
  public LoginResponseDTO login(LoginDTO loginDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          loginDTO.getPhoneNumber(),
          loginDTO.getPassword()
        )
      );

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
      
      if (userDetails instanceof UserDetailsImpl) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        loginResponseDTO.setUserId(userDetailsImpl.getUser().getId());
        loginResponseDTO.setRoleDTO(
          roleMapper.toDTO(userDetailsImpl.getUser().getRole())
        );
      }
      
      loginResponseDTO.setAccessToken(
        jwtService.generateToken(userDetails.getUsername(), Default.JWT.ACCESS_TOKEN_EXPIRATION_TIME)
      );
      loginResponseDTO.setRefreshToken(
        jwtService.generateToken(userDetails.getUsername(), Default.JWT.REFRESH_EXPIRATION_TIME)
      );

      return loginResponseDTO;
    } catch (BadCredentialsException ex) {
      throw new UnauthorizedExceptionCustomize();
    }
  }
}
