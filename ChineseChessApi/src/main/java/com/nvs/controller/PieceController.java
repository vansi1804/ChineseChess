package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.PieceDTO;
import com.nvs.service.PieceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.PIECE)
@Tag(name = "Piece", description = "Endpoints for managing Pieces")
@RequiredArgsConstructor
public class PieceController {

    private final PieceService pieceService;

    @Operation(summary = "Get all base", description = "Endpoint to get all base trainings")
    @GetMapping(value = "")
    public ResponseEntity<List<PieceDTO>> findAll() {
        return ResponseEntity.ok(pieceService.findAll());
    }

}
