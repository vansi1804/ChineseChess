package com.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.VipDTO;
import com.service.VipService;

@RestController
@RequestMapping("api/admin/vips")
public class VipController {
   
    private final VipService vipService;

    @Autowired
    public VipController(VipService vipService) {
        this.vipService = vipService;
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(vipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(vipService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody VipDTO vipDTO) {
        return ResponseEntity.ok(vipService.create(vipDTO));
    }

    @PutMapping("/id={id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody VipDTO vipDTO) {
        return ResponseEntity.ok(vipService.update(id, vipDTO));
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        vipService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
