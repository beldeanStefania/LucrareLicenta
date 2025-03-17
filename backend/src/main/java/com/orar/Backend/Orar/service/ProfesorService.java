package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.ProfesorAlreadyExistsException;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RolRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Profesor> getAll() {
        return profesorRepository.findAll();
    }

    public Profesor add(final ProfesorDTO profesorDTO) {
        return createProfesor(profesorDTO);
    }

    private Profesor createProfesor(final ProfesorDTO profesorDTO) {
        Rol profesorRole = rolRepository.findByName("PROFESOR")
                .orElseThrow(() -> new RuntimeException("Rolul PROFESOR nu există în baza de date"));

        User user = new User();
        user.setUsername(profesorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(profesorDTO.getPassword()));
        user.setRole(profesorRole); 
        userRepository.save(user); 

        Profesor profesor = new Profesor();
        profesor.setNume(profesorDTO.getNume());
        profesor.setPrenume(profesorDTO.getPrenume());
        profesor.setUser(user); 

        return profesorRepository.save(profesor);
    }

    public Profesor update(int id, ProfesorDTO profesorDTO) throws ProfesorNotFoundException {
        Profesor existingProfesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException("Profesorul nu a fost găsit"));

        existingProfesor.setNume(profesorDTO.getNume());
        existingProfesor.setPrenume(profesorDTO.getPrenume());

        User user = existingProfesor.getUser();
        if (!user.getUsername().equals(profesorDTO.getUsername())) {
            user.setUsername(profesorDTO.getUsername());
        }
        if (profesorDTO.getPassword() != null && !profesorDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(profesorDTO.getPassword()));
        }
        userRepository.save(user);

        return profesorRepository.save(existingProfesor);
    }

    public void delete(final String numeProfesor, final String prenumeProfesor) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
        var profesor = profesorRepository.findByNumeAndPrenume(numeProfesor, prenumeProfesor)
                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
        profesorRepository.delete(profesor);
    }

}
