package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.dto.OrarDetailsDTO;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.service.OrarService;
import com.orar.Backend.Orar.exception.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/orare")
public class OrarController {

    @Autowired
    private OrarService orarService;

    @Operation(summary = "Obține toate orarele", description = "Returnează o listă cu toate orarele existente")
    @GetMapping
    public List<Orar> getAll() {
        return orarService.getAll();
    }

//    @Operation(summary = "Obține orar după grupa", description = "Returnează orarul cu grupa specificata")
//    @GetMapping("/getAll/{grupa}")
//    public ResponseEntity<List<Orar>> getOrarByGrupa(@PathVariable String grupa) {
//        List<Orar> orare = orarService.getOrarByGrupa(grupa);
//        return ResponseEntity.ok(orare != null ? orare : new ArrayList<>()); // Returnăm o listă goală dacă este null
//    }

    @Operation(summary = "Obține detaliile orarului după grupă")
    @GetMapping("/getAll/{grupa}")
    public ResponseEntity<List<OrarDetailsDTO>> getOrarDetailsByGrupa(@PathVariable String grupa) {
        List<OrarDetailsDTO> orarDetails = orarService.getOrarDetailsByGrupa(grupa);
        return ResponseEntity.ok(orarDetails != null ? orarDetails : new ArrayList<>());
    }

    @GetMapping("/getAllProfesor/{profesorId}")
    public ResponseEntity<List<OrarDetailsDTO>> getOrarDetailsByProfesor(@PathVariable Integer profesorId) {
        List<OrarDetailsDTO> orarDetails = orarService.getOrarDetailsByProfesor(profesorId);
        return ResponseEntity.ok(orarDetails != null ? orarDetails : new ArrayList<>());
    }


    @Operation(summary = "Adauga orar")
    @PostMapping("/add")
    public ResponseEntity<Orar> addOrar(@Valid @RequestBody OrarDTO orarDTO) throws Exception {
        try{
            return ok(orarService.add(orarDTO));
        } catch (OrarAlreadyExistsException e) {
            return badRequest().build();
        }
    }


    @Operation(summary = "Modifica orar")
    @PutMapping("/update/{id}")
    public ResponseEntity<Orar> updateOrar(@PathVariable Integer id, @Valid @RequestBody OrarDTO orarDTO) throws Exception {
        try {
            return ok(orarService.updateOrar(id, orarDTO));
        } catch (OrarAlreadyExistsException | OrarNotFoundException e) {
            return badRequest().build();
        }
    }

    @Operation(summary = "Sterge orar")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Orar> deleteOrar(@PathVariable Integer id)  {
        try {
            orarService.deleteOrar(id);
            return ok().build();
        } catch (OrarNotFoundException e) {
            return badRequest().build();
        }
    }
}