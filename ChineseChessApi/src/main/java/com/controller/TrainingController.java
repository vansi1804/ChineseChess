package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.TrainingDTO;
import com.service.TrainingService;

@RestController
@RequestMapping("api/trainings")
public class TrainingController {
   
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping("/id={id}")
    public ResponseEntity<?> findAllChildrenById(@PathVariable long id) {
        return ResponseEntity.ok(trainingService.findAllChildrenById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.create(trainingDTO));
    }

    @PutMapping("/id={id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.update(id, trainingDTO));
    }

}
