package com.controller.player;

import java.security.NoSuchAlgorithmException;

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

import com.data.dto.creation.PlayerCreationDTO;
import com.data.dto.profile.PlayerProfileDTO;
import com.service.PlayerService;

@RestController
@RequestMapping("api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.playerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProfileById(@PathVariable long id) {
        return ResponseEntity.ok(this.playerService.findProfileById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody PlayerCreationDTO playerCreationDTO)
            throws NoSuchAlgorithmException {
        return ResponseEntity.ok(this.playerService.create(playerCreationDTO));
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestParam long id, @RequestBody PlayerProfileDTO playerProfileDTO) {
        return ResponseEntity.ok(this.playerService.updateProfileById(id, playerProfileDTO));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<?> lockById(@PathVariable int id) {
        return ResponseEntity.ok(this.playerService.lockById(id));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<?> unlockById(@PathVariable int id) {
        return ResponseEntity.ok(this.playerService.unlockById(id));
    }

}
