package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.service.PlayBoardService;
import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(summary = "Generate", description = "Endpoint to generate a new play board")
  @GetMapping(value = "/generate")
  public ResponseEntity<PlayBoardDTO> generate() {
    return ResponseEntity.ok(playBoardService.generate());
  }

}
