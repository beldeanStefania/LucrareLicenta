package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.SalaDTO;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.exception.SalaAlreadyExistsException;
import com.orar.Backend.Orar.exception.SalaNotFoundException;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Obtine toate salile")
    @GetMapping("/getAll")
    public List<Sala> getAll() {
        return salaService.getAll();
    }

    @GetMapping("/byCladire/{cladireId}")
    public ResponseEntity<List<Sala>> getSaliByCladire(@PathVariable Integer cladireId) {
        return ResponseEntity.ok(salaService.getSaliByCladire(cladireId));
    }

    @Operation(summary = "Adauga sala")
    @PostMapping("/add")
    public ResponseEntity<Sala> addSala(@RequestBody SalaDTO salaDTO) {
        try {
            return ok(salaService.add(salaDTO));
        } catch (SalaAlreadyExistsException | CladireNotFoundException e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Modifica sala")
    @PutMapping("/update/{numeSala}")
    public ResponseEntity<Sala> updateSala(@PathVariable String numeSala, @RequestBody SalaDTO salaDTO) {
        try {
            return ok(salaService.update(numeSala, salaDTO));
        } catch (CladireNotFoundException | SalaNotFoundException e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Sterge sala")
    @DeleteMapping("/delete{numeSala}")
    public ResponseEntity<Sala> deleteSala(@PathVariable String numeSala) {
        try {
            salaService.delete(numeSala);
            return ok().build();
        } catch (SalaNotFoundException e) {
            return badRequest().build();
        }
    }
}