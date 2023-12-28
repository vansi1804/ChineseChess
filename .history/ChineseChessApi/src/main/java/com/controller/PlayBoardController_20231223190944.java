package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
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
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RankDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Conflict",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/generate")
  public ResponseEntity<PlayBoardDTO> generate() {
    return ResponseEntity.ok(playBoardService.generate());
  }
}
