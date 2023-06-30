package com.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.common.Default;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.exception.InvalidException;
import com.service.JsonProcessService;
import com.service.PlayerService;
import com.util.ValidationDataUtil;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final JsonProcessService jsonProcessService;

    @Autowired
    public PlayerController(PlayerService playerService, JsonProcessService jsonProcessService) {
        this.playerService = playerService;
        this.jsonProcessService = jsonProcessService;
    }

    @GetMapping("")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "no", required = false, defaultValue = Default.Page.NO) int no,
            @RequestParam(name = "limit", required = false, defaultValue = Default.Page.LIMIT) int limit,
            @RequestParam(name = "sort-by", required = false, defaultValue = Default.Page.SORT_BY) String sortBy) {
        return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
    }

    @GetMapping("/users/id={userId}")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(playerService.findByUserId(userId));
    }

    @GetMapping("/id={id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(
            @RequestPart(name = "playerCreationDTO") String playerCreationDTOJsonString,
            @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar) {

        PlayerCreationDTO playerCreationDTO = jsonProcessService.readValue(
                playerCreationDTOJsonString, PlayerCreationDTO.class);

        Map<String, Object> validationErrors = ValidationDataUtil.errors(playerCreationDTO);
        if (!validationErrors.isEmpty()) {
            throw new InvalidException(validationErrors);
        }

        return ResponseEntity.ok(playerService.create(playerCreationDTO, fileAvatar));
    }

    @PutMapping("/id={id}")
    public ResponseEntity<?> update(@PathVariable long id,
            @RequestPart(name = "playerProfileDTO") String playerProfileDTOJsonString,
            @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar) {

        PlayerProfileDTO playerProfileDTO = jsonProcessService.readValue(
                playerProfileDTOJsonString, PlayerProfileDTO.class);

        Map<String, Object> validationErrors = ValidationDataUtil.errors(playerProfileDTO);
        if (!validationErrors.isEmpty()) {
            throw new InvalidException(validationErrors);
        }

        return ResponseEntity.ok(playerService.update(id, playerProfileDTO, fileAvatar));
    }

    @PutMapping("/id={id}/lock")
    public ResponseEntity<?> lockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.lockById(id));
    }

    @PutMapping("/id={id}/unlock")
    public ResponseEntity<?> unlockById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.unlockById(id));
    }

}
