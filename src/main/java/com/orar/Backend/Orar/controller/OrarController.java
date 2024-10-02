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
    public ResponseEntity<List<Orar>> getAllOrare() {
        List<Orar> orare = orarService.getAll();
        return ResponseEntity.ok(orare);
    }

    // Obține un orar după ID
    @Operation(summary = "Obține un orar după ID", description = "Returnează detaliile unui orar specific")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalii orar",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orar.class))),
            @ApiResponse(responseCode = "404", description = "Orar nu a fost găsit",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Orar> getOrarById(@PathVariable Integer id) throws OrarNotFoundException {
        Orar orar = orarService.getOrarById(id);
        return ResponseEntity.ok(orar);
    }

    // Adaugă un nou orar și orele sale
    @Operation(summary = "Creează un nou orar", description = "Adaugă un nou orar în sistem împreună cu orele sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orar creat",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orar.class))),
            @ApiResponse(responseCode = "409", description = "Orar deja există pentru ziua specificată sau conflict în ore",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Orar, Sala, Profesor sau Materie nu a fost găsit",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Orar> addOrar(@Valid @RequestBody OrarDTO orarDTO) throws Exception {
        Orar orar = orarService.addOrar(orarDTO);
        return ResponseEntity.status(201).body(orar);
    }

    // Actualizează un orar existent și orele sale
    @Operation(summary = "Actualizează un orar existent", description = "Modifică detaliile unui orar existent împreună cu orele sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orar actualizat",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orar.class))),
            @ApiResponse(responseCode = "404", description = "Orar nu a fost găsit",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict la actualizare sau în ore",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Orar> updateOrar(@PathVariable Integer id, @Valid @RequestBody OrarDTO orarDTO) throws Exception {
        Orar updatedOrar = orarService.updateOrar(id, orarDTO);
        return ResponseEntity.ok(updatedOrar);
    }

    // Șterge un orar după ID
    @Operation(summary = "Șterge un orar", description = "Elimină un orar din sistem împreună cu orele sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Orar șters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Orar nu a fost găsit",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrar(@PathVariable Integer id) throws OrarNotFoundException, OraNotFoundException {
        orarService.deleteOrar(id);
        return ResponseEntity.noContent().build();
    }
}
