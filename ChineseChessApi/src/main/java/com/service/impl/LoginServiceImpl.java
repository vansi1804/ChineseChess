package com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.config.exception.UnauthorizedException;
import com.config.security.JwtService;
import com.config.security.UserDetailsImpl;
import com.data.dto.login.LoginDTO;
import com.data.dto.login.LoginResponseDTO;
import com.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getPhoneNumber(), loginDTO.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

            if (userDetails instanceof UserDetailsImpl) {
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                loginResponseDTO.setUserId(userDetailsImpl.getUserId());
            }

            List<String> roleNames = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            loginResponseDTO.setRoleNames(roleNames);
            loginResponseDTO.setAccessToken(jwtService.generateToken(userDetails.getUsername()));

            return loginResponseDTO;
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException();
        }
    }
}
