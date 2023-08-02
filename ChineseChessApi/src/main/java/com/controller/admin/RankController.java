package com.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.RANKS)
public class RankController {

    private final RankService rankService;

    @Autowired
    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(rankService.findAll());
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(rankService.findById(id));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody @Valid RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.create(rankDTO));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping(value = "/id={id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid RankDTO rankDTO) {
        return ResponseEntity.ok(rankService.update(id, rankDTO));
    }

}
