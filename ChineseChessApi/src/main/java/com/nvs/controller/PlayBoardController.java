package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.service.PlayBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.PLAY_BOARD)
@Tag(name = "Play board", description = "Endpoints for managing the play board")
@RequiredArgsConstructor
public class PlayBoardController {

  private final PlayBoardService playBoardService;

  @Operation(summary = "Generate a new play board",
      description = "Creates and returns a new play board, typically used at the start of a new game or session")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Play board successfully generated"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "/generate")
  public ResponseEntity<PlayBoardDTO> generate() {
    return ResponseEntity.ok(playBoardService.generate());
  }

}
