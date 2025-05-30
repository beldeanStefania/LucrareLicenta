package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.service.MaterieService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Autowired
    private MaterieRepository materieRepository;

    @GetMapping("/search")
    public ResponseEntity<List<MaterieDTO>> search(
            @RequestParam("q") String q
    ) {
        var list = materieRepository
                .findByNumeContainingIgnoreCaseOrCodContainingIgnoreCase(q, q);

        var dtos = list.stream()
                .map(m -> new MaterieDTO(m.getNume(), m.getSemestru(), m.getCod(), m.getCredite()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtine toate materiile")
    @GetMapping("/getAll")
    public List<Materie> getAll() {
        return materieService.getAll();
    }

    @Operation(summary = "Adauga materie")
    @PostMapping("/add")
    public ResponseEntity<Materie> addMaterie(@RequestBody MaterieDTO materieDTO) {
        try {
            return ok(materieService.add(materieDTO));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Modifica materie")
    @PutMapping("/update")
    public ResponseEntity<Materie> updateMaterie(@RequestBody MaterieDTO materieDTO) {
        try {
            return ok(materieService.update(materieDTO));
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Sterge materie")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Materie> deleteMaterie(@PathVariable int id) {
        try {
            materieService.delete(id);
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }
}
