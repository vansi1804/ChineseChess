package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.PieceService;

@RestController
@RequestMapping("/api")
public class PieceController {
    @Autowired
    private PieceService pieceService;

    @GetMapping("/pieces")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(pieceService.findAll(), HttpStatus.OK);
    }

}
