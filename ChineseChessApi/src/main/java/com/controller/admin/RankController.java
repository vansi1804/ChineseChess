package com.controller.admin;

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

// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(ApiUrl.RANKS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
// @Api(tags = "Operations pertaining to rank for player in Chinese Chess. Must login with admin role.")
public class RankController {

    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping(value = "")
    // @ApiOperation(value = "Find all ranks", response = RankDTO.class)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(rankService.findAll());
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(rankService.findById(id));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.create(rankDTO));
    }

    @PutMapping(value = "/id={id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.update(id, rankDTO));
    }

}
