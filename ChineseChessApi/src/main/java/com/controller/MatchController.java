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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/matches")
public class MatchController {
    
    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/players/id={playerId}")
    public ResponseEntity<?> findAllByPlayerId(@PathVariable long playerId) {
        return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
    }

    @GetMapping("/id={id}")
    public ResponseEntity<?> findMatchDetailById(@PathVariable long id) {
        return ResponseEntity.ok(matchService.findMatchDetailById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody MatchCreationDTO matchCreationDTO) {
        return ResponseEntity.ok(matchService.create(matchCreationDTO));
    }

    @PutMapping("/id={id}/{isRedWin}")
    public ResponseEntity<?> updateResult(@PathVariable long id, @PathVariable Boolean isRedWin) {
        return ResponseEntity.ok(matchService.updateResult(id, isRedWin));
    }
    
    @PostMapping("/trainings/id={trainingId}")
    public ResponseEntity<?> create(@PathVariable long trainingId) {
        return ResponseEntity.ok(matchService.create(trainingId));
    }

}
