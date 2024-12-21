package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.exception.CladireAlreadyExistsException;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.service.CladireService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/cladire")
public class CladireController {

    @Autowired
    private CladireService cladireService;

    @Operation(summary = "Obtine toate cladirile")
    @GetMapping("/getAll")
    public List<Cladire> getAll() {
        return cladireService.getAll();
    }


    @Operation(summary = "Adauga cladire")
    @PostMapping("/add")
    public ResponseEntity<Cladire> addCladire(@RequestBody CladireDTO cladireDTO) {
        try {
            return ok(cladireService.add(cladireDTO));
        } catch (CladireAlreadyExistsException e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Modifica cladire")
    @PutMapping("/update/{numeCladire}")
    public ResponseEntity<Cladire> updateCladire(@PathVariable String numeCladire, @RequestBody CladireDTO cladireDTO) {
        try {
            return ok(cladireService.update(numeCladire, cladireDTO));
        } catch (CladireNotFoundException e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Sterge cladire")
    @DeleteMapping("/delete/{numeCladire}")
    public ResponseEntity<Cladire> deleteCladire(@PathVariable String numeCladire) {
        try {
            cladireService.delete(numeCladire);
            return ok().build();
        } catch (CladireNotFoundException e) {
            return badRequest().build();
        }
    }
}
