package com.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.service.RoleService;

@RestController
@RequestMapping(ApiUrl.ROLES)
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

}
