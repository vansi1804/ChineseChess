package com.controller.player;

import org.apache.kafka.common.security.oauthbearer.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.common.Default;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.PlayerService;

@RestController
@RequestMapping("api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "no", defaultValue = Default.Page.NO) int no,
            @RequestParam(name = "limit", defaultValue = Default.Page.LIMIT) int limit,
            @RequestParam(name = "sort-by", defaultValue = Default.Page.SORT_BY) String sortBy) {
        return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(playerService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(
            @RequestPart String playerCreationDTOJsonString,
            @RequestPart MultipartFile fileAvatar) {
        PlayerCreationDTO playerCreationDTO;
        try {
            playerCreationDTO = new ObjectMapper().readValue(
                    playerCreationDTOJsonString, PlayerCreationDTO.class);
        } catch (JsonProcessingException e) {
            throw new ValidateException(e.getMessage());
        }
        return ResponseEntity.ok(playerService.create(playerCreationDTO, fileAvatar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody PlayerProfileDTO playerProfileDTO) {
        return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<?> lockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.lockById(id));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<?> unlockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.unlockById(id));
    }

}
