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
        // Recuperăm rolul PROFESOR din baza de date
        Rol profesorRole = rolRepository.findByName("PROFESOR")
                .orElseThrow(() -> new RuntimeException("Rolul PROFESOR nu există în baza de date"));

        // Cream un User asociat cu acest Profesor
        User user = new User();
        user.setUsername(profesorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(profesorDTO.getPassword()));
        user.setRole(profesorRole); // Asociază rolul PROFESOR cu utilizatorul
        userRepository.save(user); // Salvează user-ul în baza de date

        // Cream Profesorul
        Profesor profesor = new Profesor();
        profesor.setNume(profesorDTO.getNume());
        profesor.setPrenume(profesorDTO.getPrenume());
        profesor.setUser(user); // Asociază User-ul cu Profesorul

        return profesorRepository.save(profesor);
    }

    public Profesor update(int id, ProfesorDTO profesorDTO) throws ProfesorNotFoundException {
        // Verificăm dacă profesorul există
        Profesor existingProfesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException("Profesorul nu a fost găsit"));

        // Actualizăm detaliile profesorului
        existingProfesor.setNume(profesorDTO.getNume());
        existingProfesor.setPrenume(profesorDTO.getPrenume());

        // Actualizăm datele de utilizator, dacă este necesar
        User user = existingProfesor.getUser();
        if (!user.getUsername().equals(profesorDTO.getUsername())) {
            user.setUsername(profesorDTO.getUsername());
        }
        if (profesorDTO.getPassword() != null && !profesorDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(profesorDTO.getPassword()));
        }
        userRepository.save(user); // Salvăm modificările utilizatorului

        // Salvăm profesorul actualizat
        return profesorRepository.save(existingProfesor);
    }



    public void delete(final String numeProfesor, final String prenumeProfesor) throws ProfesorAlreadyExistsException, ProfesorDoesNotExistException {
        var profesor = profesorRepository.findByNumeAndPrenume(numeProfesor, prenumeProfesor)
                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found"));
        profesorRepository.delete(profesor);
    }

}
