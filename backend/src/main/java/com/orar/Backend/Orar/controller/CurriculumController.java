package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/materii-obligatorii/{studentCod}/{anContract}")
    public ResponseEntity<List<MaterieDTO>> getMateriiObligatorii(
            @PathVariable String studentCod,
            @PathVariable int anContract) {
        try {
            List<MaterieDTO> materii = curriculumService.getMateriiObligatoriiByYear(studentCod, anContract);
            return ResponseEntity.ok(materii);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
