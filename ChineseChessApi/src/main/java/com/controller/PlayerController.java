package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.common.ApiUrl;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.service.PlayerService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.PLAYERS)
public class PlayerController {

    private final PlayerService playerService;
    // private final JsonProcessService jsonProcessService;

    @Autowired
    public PlayerController(PlayerService playerService
    // , JsonProcessService jsonProcessService
    ) {
        this.playerService = playerService;
        // this.jsonProcessService = jsonProcessService;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "no", required = false, defaultValue = Default.Page.NO) int no,
            @RequestParam(name = "limit", required = false, defaultValue = Default.Page.LIMIT) int limit,
            @RequestParam(name = "sort-by", required = false, defaultValue = Default.Page.SORT_BY) String sortBy) {
        
        return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/users/id={userId}")
    public ResponseEntity<?> findByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(playerService.findByUserId(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    // @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    // produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<?> create(
    // @RequestPart(name = "playerCreationDTO") String playerCreationDTOJsonString,
    // @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar)
    // {

    // PlayerCreationDTO playerCreationDTO = jsonProcessService.readValue(
    // playerCreationDTOJsonString, PlayerCreationDTO.class);

    // Map<String, Object> validationErrors =
    // DataValidationUtil.validate(playerCreationDTO);
    // if (validationErrors != null) {
    // throw new InvalidException(validationErrors);
    // }

    // return ResponseEntity.ok(playerService.create(playerCreationDTO,
    // fileAvatar));
    // }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @RequestPart(name = "playerCreationDTO") @Valid PlayerCreationDTO playerCreationDTO,
            @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar) {

        return ResponseEntity.ok(playerService.create(playerCreationDTO, fileAvatar));
    }

    // @PreAuthorize("isAuthenticated()")
    // @PutMapping(value = "/id={id}")
    // public ResponseEntity<?> update(@PathVariable long id,
    // @RequestPart(name = "playerProfileDTO") String playerProfileDTOJsonString,
    // @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar)
    // {

    // PlayerProfileDTO playerProfileDTO = jsonProcessService.readValue(
    // playerProfileDTOJsonString, PlayerProfileDTO.class);

    // Map<String, Object> validationErrors =
    // DataValidationUtil.validate(playerProfileDTO);
    // if (validationErrors != null) {
    // throw new InvalidException(validationErrors);
    // }

    // return ResponseEntity.ok(playerService.update(id, playerProfileDTO,
    // fileAvatar));
    // }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/id={id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable long id,
            @RequestPart(name = "playerProfileDTO") @Valid PlayerProfileDTO playerProfileDTO,
            @RequestPart(name = "fileAvatar", required = false) MultipartFile fileAvatar) {

        return ResponseEntity.ok(playerService.update(id, playerProfileDTO, fileAvatar));
    }

}
