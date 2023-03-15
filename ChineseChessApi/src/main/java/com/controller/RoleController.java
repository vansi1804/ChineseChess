package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.RoleDTO;
import com.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(this.roleService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(this.roleService.create(roleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(this.roleService.update(id, roleDTO));
    }

}
