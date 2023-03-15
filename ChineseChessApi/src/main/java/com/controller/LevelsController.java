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

import com.dto.LevelsDTO;
import com.service.LevelsService;

@RestController
@RequestMapping("/api/levels")
public class LevelsController {
    @Autowired
    private LevelsService levelsService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.levelsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(this.levelsService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody LevelsDTO levelsDTO) {
        return ResponseEntity.ok(this.levelsService.create(levelsDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody LevelsDTO levelsDTO) {
        return ResponseEntity.ok(this.levelsService.update(id, levelsDTO));
    }
}
