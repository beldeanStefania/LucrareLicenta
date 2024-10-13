package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.service.OrarService;
import com.orar.Backend.Orar.exception.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/orare")
public class OrarController {

    @Autowired
    private OrarService orarService;

    // Obține toate orarele
    @Operation(summary = "Obține toate orarele", description = "Returnează o listă cu toate orarele existente")
    @GetMapping
    public List<Orar> getAll() {
        return orarService.getAll();
    }

    @PostMapping
    public ResponseEntity<Orar> addOrar(@Valid @RequestBody OrarDTO orarDTO) throws Exception {
        try{
        return ok(orarService.add(orarDTO));
        } catch (OrarAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orar> updateOrar(@PathVariable Integer id, @Valid @RequestBody OrarDTO orarDTO) throws Exception {
        try {
            return ok(orarService.updateOrar(id, orarDTO));
        } catch (OrarAlreadyExistsException | OrarNotFoundException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Orar> deleteOrar(@PathVariable Integer id)  {
        try {
            orarService.deleteOrar(id);
            return ok().build();
        } catch (OrarNotFoundException e) {
            return badRequest().build();
        }
    }
}
