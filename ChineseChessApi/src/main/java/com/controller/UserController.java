package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.common.ApiUrl;
import com.service.UserService;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.USER)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@Api(value = "Chinese Chess API", description = "Operations pertaining to user's status. Must login by admin role.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(value = "/id={id}/lock")
    public ResponseEntity<?> lockById(@PathVariable long id) {
        return ResponseEntity.ok(userService.lockById(id));
    }

    @PutMapping(value = "/id={id}/active")
    public ResponseEntity<?> unlockById(@PathVariable long id) {
        return ResponseEntity.ok(userService.unlockById(id));
    }

}
