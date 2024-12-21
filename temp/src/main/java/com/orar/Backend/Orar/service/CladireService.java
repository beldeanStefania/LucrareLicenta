package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.exception.CladireAlreadyExistsException;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.repository.CladireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CladireService {

    @Autowired
    private CladireRepository cladireRepository;

    public List<Cladire> getAll() {
        return cladireRepository.findAll();
    }
    public Cladire add(final CladireDTO cladireDTO) throws CladireAlreadyExistsException {
        var newCladire = buildCladire(cladireDTO);
        return add(newCladire);
    }

    private Cladire buildCladire(final CladireDTO cladireDTO) throws CladireAlreadyExistsException {
        checkCladireExists(cladireDTO);
        return createCladire(cladireDTO);
    }

    private void checkCladireExists(final CladireDTO cladireDTO) throws CladireAlreadyExistsException {
        if (cladireRepository.findByNume(cladireDTO.getNume()).isPresent()) {
            throw new CladireAlreadyExistsException("Cladire already exists");
        }
    }

    public Cladire add(final Cladire cladire) {
        return cladireRepository.save(cladire);
    }

    private Cladire createCladire(final CladireDTO cladireDTO) {
        Cladire cladire = new Cladire();
        cladire.setNume(cladireDTO.getNume());
        cladire.setAdresa(cladireDTO.getAdresa());
        return cladire;
    }
    public Cladire update(String numeCladire, final CladireDTO cladireDTO) throws CladireNotFoundException {
        Cladire cladire = findCladire(numeCladire);
        cladire.setNume(cladireDTO.getNume());
        cladire.setAdresa(cladireDTO.getAdresa());
        return cladireRepository.save(cladire);
    }

    private Cladire findCladire(String numeCladire) throws CladireNotFoundException {
        return cladireRepository.findByNume(numeCladire)
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found"));
    }

    public void delete(final String numeCladire) throws CladireNotFoundException {
        var cladire = cladireRepository.findByNume(numeCladire)
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found"));
        cladireRepository.delete(cladire);
    }
}
