package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieNotFoundException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.service.CatalogStudentMaterieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
    public ResponseEntity<List<CatalogStudentMaterieDTO>> getNoteByStudent(@PathVariable String studentCod) {
        List<CatalogStudentMaterieDTO> grades = catalogStudentMaterieService.getNoteByStudent(studentCod);
        return ResponseEntity.ok(grades != null ? grades : new ArrayList<>());
    }

    @PostMapping("/add")
    public ResponseEntity<CatalogStudentMaterie> addCatalog(@RequestBody CatalogStudentMaterieDTO dto) {
        System.out.println("addCatalog endpoint was hit with: " + dto);
        try {
            CatalogStudentMaterie createdCatalog = catalogStudentMaterieService.addOrUpdate(dto);
            return ResponseEntity.status(CREATED).body(createdCatalog);
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
