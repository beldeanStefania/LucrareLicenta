package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.dto.SalaDTO;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.exception.SalaAlreadyExistsException;
import com.orar.Backend.Orar.exception.SalaNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.repository.CladireRepository;
import com.orar.Backend.Orar.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private CladireRepository cladireRepository;

    public Sala add(final SalaDTO salaDTO) throws SalaAlreadyExistsException, CladireNotFoundException {
        var newSala = buildSala(salaDTO);
        return add(newSala);
    }
    private Sala buildSala(final SalaDTO salaDTO) throws SalaAlreadyExistsException, CladireNotFoundException {
        checkSalaExists(salaDTO);
        return createSala(salaDTO);
    }
    private void checkSalaExists(final SalaDTO salaDTO) throws SalaAlreadyExistsException {
        salaRepository.findByNume(salaDTO.getNume())
                .orElseThrow(() -> new SalaAlreadyExistsException("Sala already exists"));
    }

    private void findCladire(final CladireDTO cladireDTO) throws CladireNotFoundException {
        cladireRepository.findByNume(cladireDTO.getNume())
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found"));
    }

    public Sala add(final Sala sala) {
        return salaRepository.save(sala);
    }
    private Sala createSala(final SalaDTO salaDTO) throws CladireNotFoundException {
        Sala sala = new Sala();
        sala.setNume(salaDTO.getNume());
        sala.setCapacitate(salaDTO.getCapacitate());

        Cladire cladire = cladireRepository.findByNume(salaDTO.getNumeCladire())
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found"));

        sala.setCladire(cladire);
        return sala;
    }
    public Sala update(final SalaDTO salaDTO) throws SalaNotFoundException, CladireNotFoundException {
        Sala existingSala = findSala(salaDTO);
        var updatedSala = updateSalaDetails(existingSala, salaDTO);
        return update(updatedSala);
    }
    private Sala findSala(final SalaDTO salaDTO) throws SalaNotFoundException {
        return salaRepository.findByNume(salaDTO.getNume())
                .orElseThrow(() -> new SalaNotFoundException("Sala not found"));
    }
    private Sala updateSalaDetails(final Sala existingSala, final SalaDTO salaDTO) throws CladireNotFoundException {
        existingSala.setCapacitate(salaDTO.getCapacitate());

        Cladire cladire = cladireRepository.findByNume(salaDTO.getNumeCladire())
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found"));

        existingSala.setCladire(cladire);
        return existingSala;
    }
    private Sala update(final Sala sala) {
        return salaRepository.save(sala);
    }
    public void delete(final String numeSala) throws SalaNotFoundException {
        Sala sala = salaRepository.findByNume(numeSala)
                .orElseThrow(() -> new SalaNotFoundException("Sala not found"));
        salaRepository.delete(sala);
    }
}
