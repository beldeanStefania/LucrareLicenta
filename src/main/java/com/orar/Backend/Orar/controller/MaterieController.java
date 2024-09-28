package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.service.MaterieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/materie")
public class MaterieController {

    @Autowired
    private MaterieService materieService;

    @GetMapping("/getAll")
    public List<Materie> getAll() {
        return materieService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Materie> addMaterie(@RequestBody MaterieDTO materieDTO) {
        try {
            return ok(materieService.add(materieDTO));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Materie> updateMaterie(@RequestBody MaterieDTO materieDTO) {
        try {
            return ok(materieService.update(materieDTO));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Materie> deleteMaterie(@PathVariable String numeMaterie) {
        try {
            materieService.delete(numeMaterie);
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }
}