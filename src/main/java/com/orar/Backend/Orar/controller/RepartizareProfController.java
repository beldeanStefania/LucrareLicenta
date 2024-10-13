package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.service.RepartizareProfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<RepartizareProf> addRepartizareProf(@Valid @RequestBody RepartizareProfDTO repartizareProfDTO) throws Exception {
        try {
            return ok(repartizareProfService.add(repartizareProfDTO));
        } catch (RepartizareProfAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepartizareProf> updateRepartizareProf(@PathVariable Integer id, @Valid @RequestBody RepartizareProfDTO repartizareProfDTO) throws Exception {
        try {
            return ok(repartizareProfService.updateRepartizareProf(id, repartizareProfDTO));
        } catch (RepartizareProfAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RepartizareProf> deleteRepartizareProf(@PathVariable Integer id) {
        try {
            repartizareProfService.delete(id);
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }

}
