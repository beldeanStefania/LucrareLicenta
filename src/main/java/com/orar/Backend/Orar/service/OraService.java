package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OraDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OraService {

    @Autowired
    private OraRepository oraRepository;

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private MaterieRepository materieRepository;

    @Autowired
    private SalaRepository salaRepository;

    public List<Ora> getAll() {
        return oraRepository.findAll();
    }

    public Ora add(OraDTO oraDTO) throws ProfesorNotFoundException, MaterieNotFoundException, SalaNotFoundException, OrarNotFoundException, OraAlreadyExistsException {
        var newOra = buildOra(oraDTO);
        return oraRepository.save(newOra);
    }

    private Ora buildOra(OraDTO oraDTO) throws OraAlreadyExistsException {
        checkOraExists(oraDTO);
        return createOra(oraDTO);
    }

    private void checkOraExists(OraDTO oraDTO) throws OraAlreadyExistsException {
        Orar orar;
        for
        if (oraRepository.findByOrarAndProfesorAndMaterieAndSalaAndOraInceputAndOraSfarsit(oraDTO.getOrar(), oraDTO.getProfesor(), oraDTO.getMaterie(), oraDTO.getSala(), oraDTO.getOraInceput(), oraDTO.getOraSfarsit()).isPresent()) {
            throw new OraAlreadyExistsException("Ora already exists");
        }
    }

    public Ora updateOra(Integer id, Ora oraUpdated) {

    }
    public void deleteOra(Integer id) throws OraNotFoundException {
        var ora = oraRepository.findById(id)
                .orElseThrow(() -> new OraNotFoundException("Ora not found"));
    }

}
