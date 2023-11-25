package com.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.auth.LoginDTO;
import com.service.AuthService;

@RestController
@RequestMapping(ApiUrl.AUTH)
public class AuthController {
    
    private final AuthService loginService;

    public AuthController(AuthService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok(loginService.login(loginDTO));
    }
}
