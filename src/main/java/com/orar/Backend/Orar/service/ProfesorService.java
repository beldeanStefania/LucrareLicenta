//package com.orar.Backend.Orar.service;
//
//import com.orar.Backend.Orar.dto.ProfesorDTO;
//import com.orar.Backend.Orar.exception.ProfesorAlreadyExistsException;
//import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
//import com.orar.Backend.Orar.model.Profesor;
//import com.orar.Backend.Orar.repository.ProfesorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProfesorService {
//
//    @Autowired
//    private ProfesorRepository profesorRepository;
//
//    public List<Profesor> getAllProfesors() {
//        return profesorRepository.findAll();
//    }
//
//    public Profesor add(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
//        var newProfesor = buildProfesor(profesorDTO);
//        return add(newProfesor);
//    }
//
//    private Profesor buildProfesor(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
//        checkProfesorExists(profesorDTO);
//        return createProfesor(profesorDTO);
//    }
//
//    private void checkProfesorExists(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
//        if (profesorRepository.findByNumeAndPrenume(profesorDTO.getNume(), profesorDTO.getPrenume()).isPresent()) {
//            throw new ProfesorAlreadyExistsException("Profesor already exists");
//        }
//    }
//
//    private Profesor createProfesor(final ProfesorDTO profesorDTO) {
//        Profesor profesor = new Profesor();
//        profesor.setNume(profesorDTO.getNume());
//        profesor.setPrenume(profesorDTO.getPrenume());
//        return profesor;
//    }
//
//    public Profesor add(final Profesor profesor) {
//        return profesorRepository.save(profesor);
//    }
//
//    public Profesor update(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
//        Profesor existingProfesor = findProfesor(profesorDTO);
//        var updatedProfesor = updateProfesor(profesorDTO, existingProfesor);
//        return update(updatedProfesor);
//    }
//
//    private Profesor findProfesor(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
//        return profesorRepository.findByNumeAndPrenume(profesorDTO.getNume(), profesorDTO.getPrenume())
//                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
//    }
//
//    private Profesor updateProfesor(final ProfesorDTO profesorDTO, final Profesor existingProfesor) {
//        existingProfesor.setNume(profesorDTO.getNume());
//        existingProfesor.setPrenume(profesorDTO.getPrenume());
//        return existingProfesor;
//    }
//
//    private Profesor update(final Profesor profesor) {
//        return profesorRepository.save(profesor);
//    }
//
//    public void delete(final String numeProfesor, final String prenumeProfesor) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
//        var profesor = profesorRepository.findByNumeAndPrenume(numeProfesor, prenumeProfesor)
//                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
//        profesorRepository.delete(profesor);
//    }
//
//}
