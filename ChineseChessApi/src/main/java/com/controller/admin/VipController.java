package com.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.VipDTO;
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

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(vipService.findById(id));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid VipDTO vipDTO) {
        return ResponseEntity.ok(vipService.create(vipDTO));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping(value = "/id={id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid VipDTO vipDTO) {
        return ResponseEntity.ok(vipService.update(id, vipDTO));
    }

}
