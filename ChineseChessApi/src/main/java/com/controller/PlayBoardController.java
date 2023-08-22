package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.service.PlayBoardService;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.PLAY_BOARD)
@Api(value = "Chinese Chess API", description = "Operations pertaining to game's play board.")
public class PlayBoardController {

    private final PlayBoardService playBoardService;

    @Autowired
    public PlayBoardController(PlayBoardService playBoardService) {
        this.playBoardService = playBoardService;
    }

    @GetMapping(value = "/generate")
    public ResponseEntity<?> generate() {
        return ResponseEntity.ok(playBoardService.generate());
    }

}
