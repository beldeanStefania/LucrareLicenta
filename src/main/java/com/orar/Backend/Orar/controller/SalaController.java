package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.SalaDTO;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.exception.SalaAlreadyExistsException;
import com.orar.Backend.Orar.exception.SalaNotFoundException;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/sala")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping("/getAll")
    public List<Sala> getAll() {
        return salaService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Sala> addSala(@RequestBody SalaDTO salaDTO) {
        try {
            return ok(salaService.add(salaDTO));
        } catch (SalaAlreadyExistsException | CladireNotFoundException e) {
            return badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Sala> updateSala(@RequestBody SalaDTO salaDTO) {
        try {
            return ok(salaService.update(salaDTO));
        } catch (CladireNotFoundException | SalaNotFoundException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Sala> deleteSala(@PathVariable String numeSala, @PathVariable String numeCladire) {
        try {
            salaService.delete(numeSala, numeCladire);
            return ok().build();
        } catch (SalaNotFoundException | CladireNotFoundException e) {
            return badRequest().build();
        }
    }
}
