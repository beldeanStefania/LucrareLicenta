package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.OraDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.Ora;
import com.orar.Backend.Orar.service.OraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/ora")
public class OraContoller {
    @Autowired
    private OraService oraService;

    @GetMapping
    public ResponseEntity<List<Ora>> getAll() {
        List<Ora> ore = oraService.getAll();
        return ResponseEntity.ok(ore);
    }

    @PostMapping
    public ResponseEntity<?> addOra(@Valid @RequestBody OraDTO oraDTO) {
        try {
            Ora ora = oraService.add(oraDTO);
            return ResponseEntity.status(CREATED).body(ora);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOra(@PathVariable Integer id) {
        oraService.deleteOra(id);
        return ResponseEntity.noContent().build();
    }

}
