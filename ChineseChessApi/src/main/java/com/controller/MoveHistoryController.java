package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.MoveHistoryCreationDTO;
import com.service.MoveHistoryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/moveHistories")
public class MoveHistoryController {
   
    private MoveHistoryService moveHistoryService;

    @Autowired
    public MoveHistoryController(MoveHistoryService moveHistoryService) {
        this.moveHistoryService = moveHistoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> findMoveValid(
            @RequestParam("matchId") long matchId, @RequestParam("pieceId") int pieceId) {
        return ResponseEntity.ok(moveHistoryService.findMoveValid(matchId, pieceId));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody MoveHistoryCreationDTO moveHistoryCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(moveHistoryCreationDTO));
    }

}
