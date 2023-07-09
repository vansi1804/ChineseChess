package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.MatchMoveCreationDTO;
import com.data.dto.MoveCreationDTO;
import com.data.dto.TrainingMoveCreationDTO;
import com.data.dto.ValidMoveRequestDTO;
import com.service.MoveHistoryService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.MOVE_HISTORIES)
public class MovingController {

    private MoveHistoryService moveHistoryService;

    @Autowired
    public MovingController(MoveHistoryService moveHistoryService) {
        this.moveHistoryService = moveHistoryService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findMoveValid(@Valid @RequestBody ValidMoveRequestDTO validMoveRequestDTO) {
        return ResponseEntity.ok(moveHistoryService.findMoveValid(validMoveRequestDTO));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@Valid @RequestBody MoveCreationDTO moveCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(moveCreationDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/match")
    public ResponseEntity<?> create(@Valid @RequestBody MatchMoveCreationDTO moveHistoryCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(moveHistoryCreationDTO));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "/training")
    public ResponseEntity<?> create(@Valid @RequestBody TrainingMoveCreationDTO trainingMoveHistoryCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(trainingMoveHistoryCreationDTO));
    }

}