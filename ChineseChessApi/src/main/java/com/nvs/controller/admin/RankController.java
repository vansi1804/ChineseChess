package com.nvs.controller.admin;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.RankDTO;
import com.nvs.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.RANKS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Rank", description = "Endpoints for managing ranks - requires 'ADMIN' role")
public class RankController {

  private final RankService rankService;

  @Operation(summary = "Get all ranks", description = "Retrieve a list of all ranks.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all ranks"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "")
  public ResponseEntity<List<RankDTO>> findAll() {
    return ResponseEntity.ok(rankService.findAll());
  }

  @Operation(summary = "Find a rank by ID", description = "Retrieve a rank by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the rank"),
      @ApiResponse(responseCode = "404", description = "Rank not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "/{id}")
  public ResponseEntity<RankDTO> findById(@PathVariable int id) {
    return ResponseEntity.ok(rankService.findById(id));
  }

  @Operation(summary = "Create a new rank", description = "Create a new rank with the provided details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created the rank"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "")
  public ResponseEntity<RankDTO> create(@RequestBody @Valid RankDTO rankDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(rankService.create(rankDTO));
  }

  @Operation(summary = "Update an existing rank", description = "Update an existing rank with the provided details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated the rank"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "404", description = "Rank not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping(value = "/{id}")
  public ResponseEntity<RankDTO> update(@PathVariable int id, @RequestBody @Valid RankDTO rankDTO) {
    return ResponseEntity.ok(rankService.update(id, rankDTO));
  }

}
