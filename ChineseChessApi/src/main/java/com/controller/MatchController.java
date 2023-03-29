package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.MatchCreationDTO;
import com.service.MatchService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("/players/{playerId}")
    public ResponseEntity<?> findAllByPlayerId(@PathVariable long playerId) {
        return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody MatchCreationDTO matchCreationDTO) {
        return ResponseEntity.ok(matchService.create(matchCreationDTO));
    }
    
}