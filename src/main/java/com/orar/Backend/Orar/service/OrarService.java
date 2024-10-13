package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrarService {

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private OraRepository oraRepository;

    @Autowired
    private GrupaRepository grupaRepository;

    @Autowired
    private OraService oraService;

    public List<Orar> getAll() {
        return orarRepository.findAll();
    }

    public Optional<Orar> getOrarById(Integer id) {
        return orarRepository.findById(id);
    }

    public Orar add(OrarDTO orarDTO) throws GrupaNotFoundException {

        Orar orar = new Orar();
        orar.setZiua(orarDTO.getZiua());

        Grupa grupa = grupaRepository.findById(orarDTO.getGrupaId())
                .orElseThrow(() -> new GrupaNotFoundException("Grupa nu a fost găsită!"));
        orar.setGrupa(grupa);

        Ora ora = oraRepository.findById(orarDTO.getOraId())
                .orElseThrow(() -> new RuntimeException("Ora nu a fost găsită!"));

        return orarRepository.save(orar);
    }

    public void deleteOrar(Integer id) {
        orarRepository.deleteById(id);
    }

    public Orar updateOrar(Integer id, OrarDTO orarUpdated) {
        return new Orar();
    }
//        return orarRepository.findById(id)
//                .map(orar -> {
//                    orar.setGrupa(orarUpdated.getGrupa());
//                    orar.setIntervalOrarInceput(orarUpdated.getIntervalOrarInceput());
//                    orar.setIntervalOrarSfarsit(orarUpdated.getIntervalOrarSfarsit());
//                    return orarRepository.save(orar);
//                })
//                .orElseThrow(() -> new RuntimeException("Orar nu a fost găsit!"));
//    }
}
