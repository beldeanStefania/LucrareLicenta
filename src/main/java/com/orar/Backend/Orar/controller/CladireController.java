package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.exception.CladireAlreadyExistsException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.service.CladireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/cladire")
public class CladireController {

    @Autowired
    private CladireService cladireService;

    @PostMapping("/add")
    public ResponseEntity<Cladire> addCladire(@RequestBody CladireDTO cladireDTO) {
        try {
            return ok(cladireService.add(cladireDTO));
        } catch (CladireAlreadyExistsException e) {
            return badRequest().build();
        }
    }
}
