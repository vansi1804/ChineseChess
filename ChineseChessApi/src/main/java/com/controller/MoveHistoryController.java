package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.MoveHistoryDTO;
import com.service.MoveHistoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/moveHistories")
public class MoveHistoryController {
    @Autowired 
    private MoveHistoryService moveHistoryService;

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<?> findAllByMatchId(@PathVariable long matchId) {
        return ResponseEntity.ok(moveHistoryService.findAllByMatchId(matchId));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody MoveHistoryDTO moveHistoryDTO) {
        return ResponseEntity.ok(moveHistoryService.create(moveHistoryDTO));
    }
    

}
