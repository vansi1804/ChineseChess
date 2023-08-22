package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.BestAvailableMoveRequestDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.AvailableMoveRequestDTO;
import com.service.MoveService;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.MOVE_HISTORIES)
@Api(value = "Chinese Chess API", description = "Operations pertaining to game's moving.")
public class MoveController {

    private final MoveService moveService;

    @Autowired
    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findMoveValid(@RequestBody @Valid AvailableMoveRequestDTO availableMoveRequest) {
        return ResponseEntity.ok(moveService.findAllAvailable(availableMoveRequest));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid MoveCreationDTO moveCreationDTO) {
        return ResponseEntity.ok(moveService.create(moveCreationDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/match")
    public ResponseEntity<?> create(@RequestBody @Valid MatchMoveCreationDTO moveHistoryCreationDTO) {
        return ResponseEntity.ok(moveService.create(moveHistoryCreationDTO));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "/training")
    public ResponseEntity<?> create(@RequestBody @Valid TrainingMoveCreationDTO trainingMoveHistoryCreationDTO) {
        return ResponseEntity.ok(moveService.create(trainingMoveHistoryCreationDTO));
    }

    @GetMapping(value = "/best-moves")
    public ResponseEntity<?> findAllBestMoves(
            @RequestBody @Valid BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO) {
        
        return ResponseEntity.ok(moveService.findAllBestAvailable(bestAvailableMoveRequestDTO));
    }

}
