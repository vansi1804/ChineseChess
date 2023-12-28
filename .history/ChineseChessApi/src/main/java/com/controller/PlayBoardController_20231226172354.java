package com.controller;

import com.common.ApiUrl;
import com.data.dto.PlayBoardDTO;
import com.service.PlayBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.PLAY_BOARD)
@Tag(name = "Play board", description = "Endpoints for managing play board")
@RequiredArgsConstructor
public class PlayBoardController {

  private final PlayBoardService playBoardService;

  @Operation(
    summary = "Generate",
    description = "Endpoint to generate a new play board",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PlayBoardDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/generate")
  public ResponseEntity<PlayBoardDTO> generate() {
    return ResponseEntity.ok(playBoardService.generate());
  }
}
