package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.PieceDTO;
import com.nvs.service.PieceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.PIECE)
@Tag(name = "Piece", description = "Endpoints for managing pieces")
@RequiredArgsConstructor
public class PieceController {

  private final PieceService pieceService;

  @Operation(summary = "Retrieve all pieces", description = "Fetches a list of all pieces available in the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of all pieces successfully retrieved"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "")
  public ResponseEntity<List<PieceDTO>> findAll() {
    return ResponseEntity.ok(pieceService.findAll());
  }

}
