package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.TrainingMoveHistoryCreationDTO;
import com.data.dto.ValidMoveRequestDTO;
import com.service.MoveHistoryService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.MOVE_HISTORIES)
public class MoveHistoryController {

    private MoveHistoryService moveHistoryService;

    @Autowired
    public MoveHistoryController(MoveHistoryService moveHistoryService) {
        this.moveHistoryService = moveHistoryService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findMoveValid(@Valid @RequestBody ValidMoveRequestDTO validMoveRequestDTO) {
        return ResponseEntity.ok(moveHistoryService.findMoveValid(validMoveRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@Valid @RequestBody MoveHistoryCreationDTO moveHistoryCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(moveHistoryCreationDTO));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "/training")
    public ResponseEntity<?> create(@Valid @RequestBody TrainingMoveHistoryCreationDTO trainingMoveHistoryCreationDTO) {
        return ResponseEntity.ok(moveHistoryService.create(trainingMoveHistoryCreationDTO));
    }

}
