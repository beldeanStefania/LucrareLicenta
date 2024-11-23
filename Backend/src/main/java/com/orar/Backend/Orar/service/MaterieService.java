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
        return materieRepository.save(newMaterie);
    }

    private Materie buildMaterie(final MaterieDTO materieDTO) throws MaterieAlreadyExistsException {
        checkMaterieExists(materieDTO);
        return createMaterie(materieDTO);
    }

    private void checkMaterieExists(final MaterieDTO materieDTO) throws MaterieAlreadyExistsException {
        if (materieRepository.findByCod(materieDTO.getCod()).isPresent()) {
            throw new MaterieAlreadyExistsException("Materie already exists");
        }
    }

    public Materie createMaterie(final MaterieDTO materieDTO) {
        Materie materie = new Materie();
        materie.setNume(materieDTO.getNume());
        materie.setSemestru(materieDTO.getSemestru());
        materie.setCod(materieDTO.getCod());
        return materie;
    }

    public Materie update(final int id, final MaterieDTO materieDTO) throws MaterieNotFoundException, MaterieAlreadyExistsException {
        var materie = materieRepository.findById(id)
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
        materie.setNume(materieDTO.getNume());
        materie.setSemestru(materieDTO.getSemestru());
        materie.setCod(materieDTO.getCod());
        materie.setId(materie.getId());
        return materieRepository.save(materie);
    }

    private void findMaterie(final MaterieDTO materieDTO) throws MaterieNotFoundException {
        materieRepository.findByNume(materieDTO.getNume())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
    }

    public void delete(final int id) throws MaterieNotFoundException {
        var materie = materieRepository.findById(id)
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
        materieRepository.delete(materie);
    }
}
