package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.SalaDTO;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.exception.SalaAlreadyExistsException;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/sala")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping("/add")
    public ResponseEntity<Sala> addSala(@RequestBody SalaDTO salaDTO) {
        try {
            return ok(salaService.add(salaDTO));
        } catch (SalaAlreadyExistsException | CladireNotFoundException e) {
            return badRequest().build();
        }
    }
}
