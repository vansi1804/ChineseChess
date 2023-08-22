package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.match.MatchCreationDTO;
import com.service.MatchService;

import io.swagger.annotations.Api;

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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.MATCHES)
@Api(value = "Chinese Chess API", description = "Operations pertaining to match(game is played by 2 player).")
public class MatchController {
    
    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/players/id={playerId}")
    public ResponseEntity<?> findAllByPlayerId(@PathVariable long playerId) {
        return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findDetailById(@PathVariable long id) {
        return ResponseEntity.ok(matchService.findDetailById(id));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid MatchCreationDTO matchCreationDTO) {
        return ResponseEntity.ok(matchService.create(matchCreationDTO));
    }

    @PutMapping(value = "/id={id}/{isRedWin}")
    public ResponseEntity<?> updateResult(@PathVariable long id, @PathVariable Boolean isRedWin) {
        return ResponseEntity.ok(matchService.updateResult(id, isRedWin));
    }
    
}
