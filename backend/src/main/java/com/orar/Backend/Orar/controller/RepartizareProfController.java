package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
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

    @GetMapping
    public List<RepartizareProf> getAll() {
        return repartizareProfService.getAll();
    }

    @GetMapping("/materiiProfesor/{profesorId}")
    public ResponseEntity<List<RepartizareProfDTO>> getMateriiProfesor(@PathVariable Integer profesorId) {
        try {
            return ok(repartizareProfService.getMateriiProfesor(profesorId));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @GetMapping("/materiiProfesorDistincte/{profesorId}")
    public ResponseEntity<List<String>> getMateriiDistincteProfesor(@PathVariable Integer profesorId) {
        try {
            return ok(repartizareProfService.getMateriiDistincteProfesor(profesorId));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<RepartizareProf> addRepartizareProf(@Valid @RequestBody RepartizareProfDTO repartizareProfDTO) {
        try {
            return ok(repartizareProfService.add(repartizareProfDTO));
        } catch (ProfesorNotFoundException | RepartizareProfAlreadyExistsException | MaterieDoesNotExistException e) {
            return badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepartizareProf> updateRepartizareProf(@PathVariable Integer id, @Valid @RequestBody RepartizareProfDTO repartizareProfDTO) {
        try {
            return ok(repartizareProfService.updateRepartizareProf(id, repartizareProfDTO));
        } catch (RepartizareProfAlreadyExistsException | MaterieDoesNotExistException | ProfesorNotFoundException e) {
            return badRequest().body(null);
        }
    }

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
