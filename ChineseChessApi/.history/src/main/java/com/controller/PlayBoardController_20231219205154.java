package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.PlayBoardDTO;
import com.service.PlayBoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(ApiUrl.PLAY_BOARD)
@RequiredArgsConstructor
public class PlayBoardController {

    private final PlayBoardService playBoardService;

    public PlayBoardController(PlayBoardService playBoardService) {
        this.playBoardService = playBoardService;
    }

    @Operation(summary = "Generate a Play Board", description = "Generates a new Play Board", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully", content = @Content(schema = @Schema(implementation = PlayBoardDTO.class)))
    })
    @GetMapping(value = "/generate")
    public ResponseEntity<PlayBoardDTO> generate() {
        return ResponseEntity.ok(playBoardService.generate());
    }
}
