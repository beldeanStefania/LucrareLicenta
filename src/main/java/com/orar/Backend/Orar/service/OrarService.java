package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrarService {

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private RepartizareProfRepository repartizareProfRepository;

    @Autowired
    private SalaRepository salaRepository;

    public List<Orar> getAll() {
        return orarRepository.findAll();
    }

    public Optional<Orar> getOrarById(Integer id) {
        return orarRepository.findById(id);
    }

    public Orar add(OrarDTO orarDTO) throws GrupaNotFoundException, OrarAlreadyExistsException {
        var newOrar = buildOrar(orarDTO);
        return orarRepository.save(newOrar);
    }

    private Orar buildOrar(OrarDTO orarDTO) throws OrarAlreadyExistsException {
        checkOrarExists(orarDTO);
        return createOrar(orarDTO);
    }

    /**
     *
     * @param orarDTO
     * @return
     */
    private Orar createOrar(OrarDTO orarDTO) {
        Orar orar = new Orar();
        orar.setGrupa(orarDTO.getGrupa());
        orar.setZi(orarDTO.getZi());
        orar.setOraInceput(orarDTO.getOraInceput());
        orar.setOraSfarsit(orarDTO.getOraSfarsit());
        orar.setZi(orarDTO.getZi());
        RepartizareProf repartizareProf = repartizareProfRepository.findById(orarDTO.getRepartizareProfId())
                .orElseThrow(() -> new NoSuchElementException("RepartizareProf not found"));
        orar.setRepartizareProf(repartizareProf);
        Sala sala = salaRepository.findById(orarDTO.getSalaId())
                .orElseThrow(() -> new NoSuchElementException("Sala not found"));
        orar.setSala(sala);
        return orar;
    }

    /**
     *
     * @param orarDTO
     * @throws OrarAlreadyExistsException
     */
    private void checkOrarExists(OrarDTO orarDTO) throws OrarAlreadyExistsException {
//        if (orarRepository.findByGrupaAndZi(orarDTO.getGrupa(), orarDTO.getZi()).isPresent()) {
//            throw new OrarAlreadyExistsException("Orar already exists");
//        }

        List<Orar> overlappingOrar = orarRepository.findOverlappingOrar(orarDTO.getSalaId(), orarDTO.getZi(),
                orarDTO.getOraInceput(), orarDTO.getOraSfarsit());

        if(!overlappingOrar.isEmpty()) {
            throw new OrarAlreadyExistsException("Sala este deja ocupata in intervalul specificat");
        }
    }

    public void deleteOrar(Integer id) throws OrarNotFoundException {
        var orar = orarRepository.findById(id)
                .orElseThrow(() -> new OrarNotFoundException("Orar not found"));
    }

    public Orar updateOrar(Integer id, OrarDTO orarUpdated) throws OrarNotFoundException, OrarAlreadyExistsException {
        var orar = orarRepository.findById(id)
                .orElseThrow(() -> new OrarNotFoundException("Orar not found"));
        var updatedOrar = buildOrar(orarUpdated);
        updatedOrar.setId(orar.getId());
        return orarRepository.save(orar);
    }

}
