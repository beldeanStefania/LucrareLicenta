//package com.orar.Backend.Orar.controller;
//
//import com.orar.Backend.Orar.dto.OraDTO;
//import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
//import com.orar.Backend.Orar.exception.OraDoesNotExistException;
//import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
//import com.orar.Backend.Orar.exception.SalaNotFoundException;
//import com.orar.Backend.Orar.model.Ora;
//import com.orar.Backend.Orar.service.OraService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import static org.springframework.http.ResponseEntity.ok;
//
//@RestController
//@RequestMapping("/api/ora")
//public class OraContoller {
//    @Autowired
//    private OraService oraService;
//
//    // Create a new Ora
//    @PostMapping("/create")
//    public ResponseEntity<Ora> createOra(@RequestBody OraDTO oraDTO, @RequestParam Integer orarId, @RequestParam Integer salaId) {
//        try {
//            Ora newOra = oraService.createOra(oraDTO, orarId, salaId);
//            return ok(newOra);
//        } catch (ProfesorDoesNotExistException | MaterieDoesNotExistException | SalaNotFoundException e) {
//            return ResponseEntity.badRequest().build();
//        }
//
//    }
//
//    // Get all Ora
//    @GetMapping("/all")
//    public List<Ora> getAll() {
//        return oraService.getAllOra();
//    }
//
//    // Get Ora by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Ora> getOraById(@PathVariable Integer id) {
//        try {
//            Ora ora = oraService.getOraById(id);
//            return ok(ora);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//
//        }
//    }
//
//        // Update an existing Ora
//        @PutMapping("/update/{id}")
//        public ResponseEntity<Ora> updateOra (@PathVariable Integer id, @RequestBody OraDTO
//        oraDTO, @RequestParam Integer salaId) throws ProfesorDoesNotExistException, MaterieDoesNotExistException, SalaNotFoundException, OraDoesNotExistException {
//            Ora updatedOra = oraService.updateOra(id, oraDTO, salaId);
//            return ok(updatedOra);
//        }
//
//        // Delete Ora
//        @DeleteMapping("/delete/{id}")
//        public ResponseEntity<Ora> deleteOra (@PathVariable Integer id){
//            try {
//                oraService.deleteOra(id);
//                return ResponseEntity.noContent().build();
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().build();
//            }
//        }
//    }
