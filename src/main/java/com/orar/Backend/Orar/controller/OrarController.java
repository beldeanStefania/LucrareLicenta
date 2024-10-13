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

@RestController
@RequestMapping("/api/orare")
public class OrarController {

    @Autowired
    private OrarService orarService;

    // Obține toate orarele
    @Operation(summary = "Obține toate orarele", description = "Returnează o listă cu toate orarele existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de orare",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Orar.class))))
    })
    @GetMapping
    public ResponseEntity<List<Orar>> getAll() {
        List<Orar> orare = orarService.getAll();
        return ResponseEntity.ok(orare);
    }

    @PostMapping
    public ResponseEntity<Orar> addOrar(@Valid @RequestBody OrarDTO orarDTO) throws Exception {
        Orar orar = orarService.add(orarDTO);
        return ResponseEntity.status(201).body(orar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orar> updateOrar(@PathVariable Integer id, @Valid @RequestBody OrarDTO orarDTO) throws Exception {
        Orar updatedOrar = orarService.updateOrar(id, orarDTO);
        return ResponseEntity.ok(updatedOrar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrar(@PathVariable Integer id) throws OrarNotFoundException, OraNotFoundException {
        orarService.deleteOrar(id);
        return ResponseEntity.noContent().build();
    }
}
