package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.exception.MaterieAlreadyExistsException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.MaterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterieService {

    @Autowired
    private MaterieRepository materieRepository;

    public List<Materie> getAll() {
        return materieRepository.findAll();
    }

    public Materie add(final MaterieDTO materieDTO) throws MaterieAlreadyExistsException {
        var newMaterie = buildMaterie(materieDTO);
        return add(newMaterie);
    }

    private Materie buildMaterie(final MaterieDTO materieDTO) throws MaterieAlreadyExistsException {
        checkMaterieExists(materieDTO);
        return createMaterie(materieDTO);
    }

    private void checkMaterieExists(final MaterieDTO materieDTO) throws MaterieAlreadyExistsException {
        if (materieRepository.findByNume(materieDTO.getNume()).isPresent()) {
            throw new MaterieAlreadyExistsException("Materie already exists");
        }
    }

    public Materie createMaterie(final MaterieDTO materieDTO) {
        Materie materie = new Materie();
        materie.setNume(materieDTO.getNume());
        materie.setSemestru(materieDTO.getSemestru());
        return materie;
    }
    public Materie add(final Materie materie) {
        return materieRepository.save(materie);
    }

    public Materie update(final MaterieDTO materieDTO) throws MaterieNotFoundException {
        findMaterie(materieDTO);
        var newMaterie = addNumeSemestruToMaterie(materieDTO);
        return update(newMaterie);
    }

    private Materie addNumeSemestruToMaterie(final MaterieDTO materieDTO) {
        //findMaterie(materieDTO);

        Materie materie = new Materie();
        materie.setNume(materieDTO.getNume());
        materie.setSemestru(materieDTO.getSemestru());
        return materie;
    }

    private void findMaterie(final MaterieDTO materieDTO) throws MaterieNotFoundException {
        materieRepository.findByNume(materieDTO.getNume())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
    }

    private Materie update(final Materie materie) {
        return materieRepository.save(materie);
    }

    public void delete(final String numeMaterie) throws MaterieNotFoundException {
        var materie = materieRepository.findByNume(numeMaterie)
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
        materieRepository.delete(materie);
    }
}
