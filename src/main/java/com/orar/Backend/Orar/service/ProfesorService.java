package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.ProfesorAlreadyExistsException;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public List<Profesor> getAll() {
        return profesorRepository.findAll();
    }

    public Profesor add(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
        var newProfesor = buildProfesor(profesorDTO);
        return profesorRepository.save(newProfesor);
    }

    private Profesor buildProfesor(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
        checkProfesorExists(profesorDTO);
        return createProfesor(profesorDTO);
    }

    private void checkProfesorExists(final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException {
        if (profesorRepository.findByNumeAndPrenume(profesorDTO.getNume(), profesorDTO.getPrenume()).isPresent()) {
            throw new ProfesorAlreadyExistsException("Profesor already exists");
        }
    }

    private Profesor createProfesor(final ProfesorDTO profesorDTO) {
        Profesor profesor = new Profesor();
        profesor.setNume(profesorDTO.getNume());
        profesor.setPrenume(profesorDTO.getPrenume());
        return profesor;
    }

    public Profesor update(int id, final ProfesorDTO profesorDTO) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
        var profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
        var updatedProfesor = buildProfesor(profesorDTO);
        updatedProfesor.setId(profesor.getId());

        return profesorRepository.save(updatedProfesor);
    }



    public void delete(final String numeProfesor, final String prenumeProfesor) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
        var profesor = profesorRepository.findByNumeAndPrenume(numeProfesor, prenumeProfesor)
                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
        profesorRepository.delete(profesor);
    }

}
