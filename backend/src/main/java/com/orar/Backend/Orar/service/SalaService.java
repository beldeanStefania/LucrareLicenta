package com.orar.Backend.Orar.service;

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

import java.util.List;

@Service
public class SalaService {


    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private CladireRepository cladireRepository;

    public List<Sala> getAll() {

        return salaRepository.findAll();
    }

    public Sala add(final SalaDTO salaDTO) throws SalaAlreadyExistsException, CladireNotFoundException {
        var newSala = buildSala(salaDTO);
        return salaRepository.save(newSala);
    }

    private Sala buildSala(final SalaDTO salaDTO) throws SalaAlreadyExistsException, CladireNotFoundException {
        checkSalaExists(salaDTO);
        Cladire cladire = findCladireByNume(salaDTO.getNumeCladire());
        return createSala(salaDTO, cladire);
    }

    private void checkSalaExists(final SalaDTO salaDTO) throws SalaAlreadyExistsException {
        if (salaRepository.findByNume(salaDTO.getNume()).isPresent()) {
            throw new SalaAlreadyExistsException("Sala already exists");
        }
    }

    private Cladire findCladireByNume(String numeCladire) throws CladireNotFoundException {
        return cladireRepository.findByNume(numeCladire)
                .orElseThrow(() -> new CladireNotFoundException("Cladire not found with nume " + numeCladire));
    }

    private Sala createSala(final SalaDTO salaDTO, Cladire cladire) {
        Sala sala = new Sala();
        sala.setNume(salaDTO.getNume());
        sala.setCapacitate(salaDTO.getCapacitate());
        sala.setCladire(cladire);
        return sala;
    }

    public Sala update(final String numeSala, final SalaDTO salaDTO) throws SalaNotFoundException, CladireNotFoundException {
        Sala sala = salaRepository.findByNume(numeSala)
                .orElseThrow(() -> new SalaNotFoundException("Sala not found with nume " + salaDTO.getNume()));
        Cladire cladire = findCladireByNume(salaDTO.getNumeCladire());

        sala.setCapacitate(salaDTO.getCapacitate());
        sala.setNume(salaDTO.getNume());
        sala.setCladire(cladire);

        return salaRepository.save(sala);
    }

    public void delete(final String numeSala) throws SalaNotFoundException {
        Sala sala = salaRepository.findByNume(numeSala)
                .orElseThrow(() -> new SalaNotFoundException("Sala not found with nume " + numeSala));
        salaRepository.delete(sala);
    }

    public Sala getSalaByNume(String nume) throws SalaNotFoundException {
        return salaRepository.findByNume(nume)
                .orElseThrow(() -> new SalaNotFoundException("Sala not found with nume " + nume));
    }

    public List<Sala> getSaliByCladire(Integer cladireId) {
        return salaRepository.findByCladireId(cladireId);
    }

}
