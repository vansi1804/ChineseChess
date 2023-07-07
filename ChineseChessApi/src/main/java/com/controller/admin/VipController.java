package com.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.service.VipService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.VIPS)
public class VipController {

    private final VipService vipService;

    @Autowired
    public VipController(VipService vipService) {
        this.vipService = vipService;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(vipService.findAll());
    }

}
