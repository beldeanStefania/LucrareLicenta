package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.GrupaDTO;
import com.orar.Backend.Orar.model.Grupa;
import com.orar.Backend.Orar.service.GrupaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/grupa")
public class GrupaController {

    @Autowired
    private GrupaService grupaService;

    @GetMapping("/getAll")
    public List<Grupa> getAll() {
        return grupaService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Grupa> addGrupa(GrupaDTO grupaDTO) {
        return ok(grupaService.add(grupaDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Grupa> deleteGrupa(@PathVariable int id) {
        grupaService.delete(id);
        return ok().build();
    }
}
