package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieAlreadyExistsException;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieNotFoundException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.service.CatalogStudentMaterieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/catalogStudentMaterie")
public class CatalogStudentMaterieController {
    @Autowired
    private CatalogStudentMaterieService catalogStudentMaterieService;

    @GetMapping("/getAll")
    public List<CatalogStudentMaterie> getAll() {
        return catalogStudentMaterieService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<CatalogStudentMaterie> addCatalog(@RequestBody CatalogStudentMaterieDTO dto) {
        try {
            CatalogStudentMaterie createdCatalog = catalogStudentMaterieService.add(dto);
            return ResponseEntity.status(CREATED).body(createdCatalog);
        } catch (CatalogStudentMaterieAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).build();
        } catch (StudentNotFoundException | MaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CatalogStudentMaterie> updateCatalog(@PathVariable Integer id, @RequestBody CatalogStudentMaterieDTO dto) {
        try {
            CatalogStudentMaterie updatedCatalog = catalogStudentMaterieService.update(id, dto);
            return ResponseEntity.ok(updatedCatalog);
        } catch (CatalogStudentMaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (StudentNotFoundException | MaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCatalog(
            @PathVariable String numeStudent,
            @PathVariable String prenumeStudent,
            @PathVariable Integer materieId) {
        try {
            catalogStudentMaterieService.deleteByStudentAndMaterie(numeStudent, prenumeStudent, materieId);
            return ResponseEntity.noContent().build();
        } catch (CatalogStudentMaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }
}
