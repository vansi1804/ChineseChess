package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.ApiUrl;
import com.data.dto.training.TrainingDTO;
import com.service.TrainingService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiUrl.TRAININGS)
public class TrainingController {
   
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(trainingService.findAllChildrenById(null));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/id={id}/children")
    public ResponseEntity<?> findAllChildrenById(@PathVariable long id) {
        return ResponseEntity.ok(trainingService.findAllChildrenById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/id={id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok(trainingService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/id={id}/details")
    public ResponseEntity<?> findDetailsById(@PathVariable long id) {
        return ResponseEntity.ok(trainingService.findDetailById(id));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@Valid @RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.create(trainingDTO));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping(value = "/id={id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.update(id, trainingDTO));
    }

	@PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping(value = "/id={id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        return ResponseEntity.ok(trainingService.deleteById(id));
    }

}
