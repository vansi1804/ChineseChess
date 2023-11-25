package com.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.Default;
import com.common.ApiUrl;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.service.PlayerService;

@RestController
@RequestMapping(ApiUrl.PLAYERS)
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "no", required = false, defaultValue = Default.Page.NO) int no,
            @RequestParam(name = "limit", required = false, defaultValue = Default.Page.LIMIT) int limit,
            @RequestParam(name = "sort-by", required = false, defaultValue = Default.Page.SORT_BY) String sortBy) {

        return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/users/id={userId}")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(playerService.findByUserId(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid PlayerCreationDTO playerCreationDTO) {
        return ResponseEntity.ok(playerService.create(playerCreationDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/id={id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid PlayerProfileDTO playerProfileDTO) {
        return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
    }

}
