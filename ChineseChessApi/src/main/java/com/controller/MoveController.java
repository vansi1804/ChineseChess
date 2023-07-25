package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.BestMoveRequestDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.AvailableMoveRequest;
import com.service.MoveService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.MOVE_HISTORIES)
public class MoveController {

    private MoveService moveService;

    @Autowired
    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findMoveValid(@Valid @RequestBody AvailableMoveRequest validMoveRequestDTO) {
        return ResponseEntity.ok(moveService.findAllAvailableMoves(validMoveRequestDTO));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@Valid @RequestBody MoveCreationDTO moveCreationDTO) {
        return ResponseEntity.ok(moveService.create(moveCreationDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/match")
    public ResponseEntity<?> create(@Valid @RequestBody MatchMoveCreationDTO moveHistoryCreationDTO) {
        return ResponseEntity.ok(moveService.create(moveHistoryCreationDTO));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "/training")
    public ResponseEntity<?> create(@Valid @RequestBody TrainingMoveCreationDTO trainingMoveHistoryCreationDTO) {
        return ResponseEntity.ok(moveService.create(trainingMoveHistoryCreationDTO));
    }

     @GetMapping(value = "/best-moves")
    public ResponseEntity<?> findAllBestMoves(@Valid @RequestBody BestMoveRequestDTO bestMoveRequestDTO) {
        return ResponseEntity.ok(moveService.findAllBestMoves(bestMoveRequestDTO));
    }

}
