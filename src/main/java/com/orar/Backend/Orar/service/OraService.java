//package com.orar.Backend.Orar.service;
//
//import com.orar.Backend.Orar.dto.OraDTO;
//import com.orar.Backend.Orar.exception.*;
//import com.orar.Backend.Orar.model.*;
//import com.orar.Backend.Orar.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class OraService {
//
//    @Autowired
//    private OraRepository oraRepository;
//
//    @Autowired
//    private ProfesorRepository profesorRepository;
//
//    @Autowired
//    private MaterieRepository materieRepository;
//
//    @Autowired
//    private SalaRepository salaRepository;
//
//    public List<Ora> getAll() {
//        return oraRepository.findAll();
//    }
//
//    // Create a new Ora and save it
//    public Ora createOra(OraDTO oraDTO, Integer orarId, Integer salaId) throws ProfesorDoesNotExistException, MaterieDoesNotExistException, SalaNotFoundException {
//        // Find Sala by its ID
//        Sala sala = salaRepository.findById(salaId)
//                .orElseThrow(() -> new SalaNotFoundException("Sala not found"));
//
//        // Find Profesor by name (using NumePrenume in DTO)
//        List<Profesor> profesorList = profesorRepository.findByNumeAndPrenume(
//                oraDTO.getNumePrenumeProfesor().nume(),
//                oraDTO.getNumePrenumeProfesor().prenume()
//        );
//
//        if (profesorList.isEmpty()) {
//            throw new ProfesorDoesNotExistException("Profesor not found");
//        }
//
//        // Find Materie by name
//        Materie materie = materieRepository.findByNume(oraDTO.getMaterie())
//                .orElseThrow(() -> new MaterieDoesNotExistException("Materie not found"));
//
//        // Create a new Ora
//        Ora ora = new Ora();
//        ora.setTip(oraDTO.getTip());  // Set TipOra
//        ora.setSala(sala);            // Associate Sala
//        ora.setProfesor(profesorList);  // Associate List of Profesori
//        ora.getMaterii().add(materie);  // Associate Materie
//
//        // Save the Ora entity
//        return oraRepository.save(ora);
//    }
//
//    // Fetch all Ora
//    public List<Ora> getAllOra() {
//        return oraRepository.findAll();
//    }
//
//    // Fetch a specific Ora by its ID
//    public Ora getOraById(Integer id) throws OraDoesNotExistException {
//        return oraRepository.findById(id)
//                .orElseThrow(() -> new OraDoesNotExistException("Ora not found with id " + id));
//    }
//
//    // Update Ora
//    public Ora updateOra(Integer id, OraDTO oraDTO, Integer salaId) throws ProfesorDoesNotExistException, OraDoesNotExistException, MaterieDoesNotExistException, SalaNotFoundException {
//        // Find the existing Ora by its ID
//        Ora existingOra = oraRepository.findById(id)
//                .orElseThrow(() -> new OraDoesNotExistException("Ora not found"));
//
//        // Update Sala
//        Sala sala = salaRepository.findById(salaId)
//                .orElseThrow(() -> new SalaNotFoundException("Sala not found"));
//        existingOra.setSala(sala);
//
//        // Update Materie
//        Materie materie = materieRepository.findByNume(oraDTO.getMaterie())
//                .orElseThrow(() -> new MaterieDoesNotExistException("Materie not found"));
//        existingOra.getMaterii().clear();
//        existingOra.getMaterii().add(materie);
//
//        // Update Profesor (assuming one or more professors)
//        List<Profesor> profesorList = profesorRepository.findByNumeAndPrenume(
//                oraDTO.getNumePrenumeProfesor().nume(),
//                oraDTO.getNumePrenumeProfesor().prenume()
//        );
//        if (profesorList.isEmpty()) {
//            throw new ProfesorDoesNotExistException("Profesor not found");
//        }
//        existingOra.setProfesor(profesorList);
//
//        // Update the TipOra
//        existingOra.setTip(oraDTO.getTip());
//
//        // Save the updated Ora
//        return oraRepository.save(existingOra);
//    }
//
//    // Delete Ora by ID
//    public void deleteOra(Integer id) throws OraDoesNotExistException {
//        Ora ora = oraRepository.findById(id)
//                .orElseThrow(() -> new OraDoesNotExistException("Ora not found"));
//        oraRepository.delete(ora);
//    }
//}
