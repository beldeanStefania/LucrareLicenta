package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.ProfesorAlreadyExistsException;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/profesor")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping("/getAll")
    public List<Profesor> getAll() {
        return profesorService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Profesor> addProfesor(@RequestBody ProfesorDTO profesorDTO) {
        try{
            return ok(profesorService.add(profesorDTO));
        } catch (ProfesorAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Profesor> updateProfesor(@PathVariable int id, @RequestBody ProfesorDTO profesorDTO) {
        try {
            return ok(profesorService.update(id, profesorDTO));
        } catch (ProfesorAlreadyExistsException | ProfesorDoesNotExistException e) {
            return badRequest().build();
        }
    }

    @DeleteMapping("/delete/{nume}/{prenume}")
    public ResponseEntity<Profesor> deleteProfesor(@PathVariable String nume, @PathVariable String prenume) {
        try {
            profesorService.delete(nume, prenume);
            return ok().build();
        } catch (ProfesorDoesNotExistException | ProfesorAlreadyExistsException e) {
            return badRequest().build();
        }
    }

}
