package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.LoginDTO;
import com.service.LoginService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.LOGIN)
public class LoginController {
    
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(loginService.login(loginDTO));
    }
}
