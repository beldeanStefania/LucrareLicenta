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
    public List<CatalogStudentMaterieDTO> getAll() {
        return catalogStudentMaterieService.getAll();
    }

    @GetMapping("/getNote/{studentCod}")
    public List<CatalogStudentMaterieDTO> getNoteByStudent(@PathVariable String studentCod) {
        return catalogStudentMaterieService.getNoteByStudent(studentCod);
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


    @PutMapping("/update/{studentCod}/{materieCod}")
    public ResponseEntity<CatalogStudentMaterie> updateCatalog(
            @PathVariable String studentCod,
            @PathVariable String materieCod,
            @RequestBody CatalogStudentMaterieDTO dto) {
        try {
            CatalogStudentMaterie updatedCatalog = catalogStudentMaterieService.update(studentCod, materieCod, dto);
            return ResponseEntity.ok(updatedCatalog);
        } catch (CatalogStudentMaterieNotFoundException | StudentNotFoundException | MaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }


    @DeleteMapping("/delete/{studentCod}/{materieCod}")
    public ResponseEntity<Void> deleteCatalog(
            @PathVariable String studentCod,
            @PathVariable String materieCod) {
        try {
            catalogStudentMaterieService.deleteByStudentAndMaterie(studentCod, materieCod);
            return ResponseEntity.noContent().build();
        } catch (CatalogStudentMaterieNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

}
