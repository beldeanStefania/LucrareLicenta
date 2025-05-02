package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.service.SpecializareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specializare")
public class SpecializareController {

    @Autowired
    private SpecializareService specializareService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Specializare>> getAllSpecializari() {
        List<Specializare> specializari = specializareService.getAll();
        return ResponseEntity.ok(specializari);
    }
}
