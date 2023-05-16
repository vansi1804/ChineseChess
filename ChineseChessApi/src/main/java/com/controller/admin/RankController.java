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

import com.data.dto.RankDTO;
import com.service.RankService;

@RestController
@RequestMapping("api/admin/ranks")
public class RankController {
    @Autowired
    private RankService rankService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(rankService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(rankService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.create(rankDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.update(id, rankDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        rankService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
