package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RepartizareProfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepartizareProfService {

    @Autowired
    private RepartizareProfRepository repartizareProfRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private MaterieRepository materieRepository;

    public List<RepartizareProf> getAll() {
        return repartizareProfRepository.findAll();
    }

    public RepartizareProf add(RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException, MaterieDoesNotExistException, ProfesorNotFoundException {
        var newRepartizareProf = buildRepartizareProf(repartizareProfDTO);
        return repartizareProfRepository.save(newRepartizareProf);

    }

    private RepartizareProf buildRepartizareProf(RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException, MaterieDoesNotExistException, ProfesorNotFoundException {
        checkRepartizareProfExists(repartizareProfDTO);
        return createRepartizareProf(repartizareProfDTO);
    }

    private RepartizareProf createRepartizareProf(RepartizareProfDTO repartizareProfDTO) throws MaterieDoesNotExistException, ProfesorNotFoundException {
        var repartizareProf = new RepartizareProf();
        var materie = materieRepository.findByNume(repartizareProfDTO.getMaterie());
        if(!materie.isPresent()) throw new MaterieDoesNotExistException("Materia nu exista");
        repartizareProf.setMaterie(materie.get());
        var profesor = profesorRepository.findByNumeAndPrenume(repartizareProfDTO.getNumeProfesor(), repartizareProfDTO.getPrenumeProfesor());
        if(!profesor.isPresent()) throw new ProfesorNotFoundException("Profesorul nu exista");
        repartizareProf.setProfesor(profesor.get());
        repartizareProf.setTip(repartizareProfDTO.getTip());
        return repartizareProf;
    }

    private void checkRepartizareProfExists(RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException {
        Profesor profesor = profesorRepository.findByNumeAndPrenume(repartizareProfDTO.getNumeProfesor(), repartizareProfDTO.getPrenumeProfesor()).get();
        Materie materie = materieRepository.findByNume(repartizareProfDTO.getMaterie()).get();
        var repartizareProf = repartizareProfRepository.findByProfesorAndMaterieAndTip(profesor, materie, repartizareProfDTO.getTip());
        if (repartizareProf.isPresent()) {
            throw new RepartizareProfAlreadyExistsException("RepartizareProf already exists");
        }
    }

    public void delete(Integer id) {
        var repartizareProf = repartizareProfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RepartizareProf not found"));
        repartizareProfRepository.delete(repartizareProf);
    }

    public RepartizareProf updateRepartizareProf(Integer id, RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException, MaterieDoesNotExistException, ProfesorNotFoundException {
        var repartizareProf = repartizareProfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RepartizareProf not found"));
        var updatedRepartizareProf = buildRepartizareProf(repartizareProfDTO);
        updatedRepartizareProf.setId(repartizareProf.getId());
        return repartizareProfRepository.save(repartizareProf);
    }

    public List<RepartizareProfDTO> getMateriiProfesor(Integer profesorId) throws ProfesorNotFoundException {
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new ProfesorNotFoundException("Profesor not found"));

        List<RepartizareProf> repartizari = repartizareProfRepository.findByProfesor(profesor);

        return repartizari.stream()
                .map(repartizare -> {
                    RepartizareProfDTO dto = new RepartizareProfDTO();
                    dto.setId(repartizare.getId());
                    dto.setMaterie(repartizare.getMaterie().getNume());
                    dto.setTip(repartizare.getTip());
                    dto.setNumeProfesor(repartizare.getProfesor().getNume());
                    dto.setPrenumeProfesor(repartizare.getProfesor().getPrenume());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<String> getMateriiDistincteProfesor(Integer profesorId) throws ProfesorNotFoundException {
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new ProfesorNotFoundException("Profesor not found"));

        return repartizareProfRepository.findByProfesor(profesor).stream()
                .map(repartizare -> repartizare.getMaterie().getNume()) 
                .distinct() 
                .collect(Collectors.toList());
    }


}