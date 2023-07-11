package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.service.PlayBoardService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.PLAY_BOARD)
public class PlayBoardController {

    private final PlayBoardService playBoardDTOService;

    @Autowired
    public PlayBoardController(PlayBoardService playBoardDTOService) {
        this.playBoardDTOService = playBoardDTOService;
    }

    @GetMapping(value = "/generate")
    public ResponseEntity<?> generate() {
        return ResponseEntity.ok(playBoardDTOService.generate());
    }

}
