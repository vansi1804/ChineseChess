package com.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.RankDTO;
import com.service.RankService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiUrl.RANKS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequiredArgsConstructor
@Tag(
  name = "Rank",
  description = "Endpoints for managing Vips - requires 'ADMIN' role"
)
public class RankController {

  private final RankService rankService;

  @Operation(
    summary = "Get all ranks",
    description = "Endpoint to retrieve all ranks"
  )
  @GetMapping(value = "")
  public ResponseEntity<List<RankDTO>> findAll() {
    return ResponseEntity.ok(rankService.findAll());
  }

  @Operation(
    summary = "Find a rank by id",
    description = "Endpoint to retrieve a rank by its id"
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<RankDTO> findById(
    @PathVariable int id
  ) {
    return ResponseEntity.ok(rankService.findById(id));
  }

  @Operation(
    summary = "Create a rank",
    description = "Endpoint to create a new rank"
  )
  @PostMapping(value = "")
  public ResponseEntity<RankDTO> create(
   @RequestBody @Valid RankDTO rankDTO
  ) {
    return ResponseEntity.ok(rankService.create(rankDTO));
  }

  @Operation(
    summary = "Update a rank",
    description = "Endpoint to update an existing rank"
  )
  @PutMapping(value = "/{id}")
  public ResponseEntity<RankDTO> update(
    @PathVariable int id,
     @RequestBody @Valid RankDTO rankDTO
  ) {
    return ResponseEntity.ok(rankService.update(id, rankDTO));
  }
}
