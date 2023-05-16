package com.controller.player;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.Default;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.service.PlayerService;

@RestController
@RequestMapping("api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "no", defaultValue = Default.PAGE_NO) int no,
            @RequestParam(name = "limit", defaultValue = Default.PAGE_LIMIT) int limit,
            @RequestParam(name = "sort-by", defaultValue = Default.SORT_BY) String sortBy) {
        return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(playerService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody PlayerCreationDTO playerCreationDTO) {
        return ResponseEntity.ok(playerService.create(playerCreationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody PlayerProfileDTO playerProfileDTO) {
        return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<?> lockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.lockById(id));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<?> unlockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.unlockById(id));
    }

}
