package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.service.RepartizareProfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/repartizareProf")
public class RepartizareProfController {

    @Autowired
    private RepartizareProfService repartizareProfService;

    /**
     * Endpoint pentru a obține toate repartizările profesorilor
     */
    @GetMapping
    public List<RepartizareProf> getAll() {
        return repartizareProfService.getAll();
    }

    /**
     * Endpoint pentru a obține repartizările unui profesor specific
     */
    @GetMapping("/materiiProfesor/{profesorId}")
    public ResponseEntity<List<RepartizareProfDTO>> getMateriiProfesor(@PathVariable Integer profesorId) {
        try {
            return ok(repartizareProfService.getMateriiProfesor(profesorId));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    /**
     * Endpoint pentru a adăuga o repartizare
     */
    @PostMapping
    public ResponseEntity<RepartizareProf> addRepartizareProf(@Valid @RequestBody RepartizareProfDTO repartizareProfDTO) {
        try {
            return ok(repartizareProfService.add(repartizareProfDTO));
        } catch (RepartizareProfAlreadyExistsException e) {
            return badRequest().body(null);
        }
    }

    /**
     * Endpoint pentru a actualiza o repartizare
     */
    @PutMapping("/{id}")
    public ResponseEntity<RepartizareProf> updateRepartizareProf(@PathVariable Integer id, @Valid @RequestBody RepartizareProfDTO repartizareProfDTO) {
        try {
            return ok(repartizareProfService.updateRepartizareProf(id, repartizareProfDTO));
        } catch (RepartizareProfAlreadyExistsException e) {
            return badRequest().body(null);
        }
    }

    /**
     * Endpoint pentru a șterge o repartizare
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepartizareProf(@PathVariable Integer id) {
        try {
            repartizareProfService.delete(id);
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }
}
