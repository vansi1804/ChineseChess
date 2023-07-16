package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.config.exception.UnauthorizedException;
import com.config.security.JwtService;
import com.data.dto.login.LoginDTO;
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
    public String login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getPhoneNumber(), loginDTO.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtService.generateToken(userDetails.getUsername());
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException();
        }

    }
}
