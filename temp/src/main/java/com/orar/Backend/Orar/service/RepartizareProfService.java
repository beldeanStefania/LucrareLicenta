package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.OrarRepository;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RepartizareProfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepartizareProfService {

    @Autowired
    private RepartizareProfRepository repartizareProfRepository;

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private MaterieRepository materieRepository;

    public List<RepartizareProf> getAll() {
        return repartizareProfRepository.findAll();
    }

    public RepartizareProf add(RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException {
        var newRepartizareProf = buildRepartizareProf(repartizareProfDTO);
        return repartizareProfRepository.save(newRepartizareProf);

    }

    private RepartizareProf buildRepartizareProf(RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException {
        checkRepartizareProfExists(repartizareProfDTO);
        return createRepartizareProf(repartizareProfDTO);
    }

    private RepartizareProf createRepartizareProf(RepartizareProfDTO repartizareProfDTO) {
        RepartizareProf repartizareProf = new RepartizareProf();
        Materie materie = materieRepository.findByNume(repartizareProfDTO.getMaterie()).get();
        repartizareProf.setMaterie(materie);
        Profesor profesor = profesorRepository.findByNumeAndPrenume(repartizareProfDTO.getNumeProfesor(), repartizareProfDTO.getPrenumeProfesor()).get();
        repartizareProf.setTip(repartizareProfDTO.getTip());
        repartizareProf.setProfesor(profesor);
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

    public RepartizareProf updateRepartizareProf(Integer id, RepartizareProfDTO repartizareProfDTO) throws RepartizareProfAlreadyExistsException {
        var repartizareProf = repartizareProfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RepartizareProf not found"));
        var updatedRepartizareProf = buildRepartizareProf(repartizareProfDTO);
        updatedRepartizareProf.setId(repartizareProf.getId());
        return repartizareProfRepository.save(repartizareProf);
    }
}
