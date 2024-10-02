package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OraDTO;
import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrarService {

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private OraRepository oraRepository;

    @Autowired
    private OraService oraService; // Pentru a reutiliza logica din OraService
    public List<Orar> getAll() {
        return orarRepository.findAll();
    }

    public Orar getOrarById(Integer id) throws OrarNotFoundException {
        return orarRepository.findById(id)
                .orElseThrow(() -> new OrarNotFoundException("Orar not found with ID: " + id));
    }

    // Adaugă un nou orar și orele sale

    public Orar addOrar(OrarDTO orarDTO) throws Exception {
        // Verifică dacă există deja un orar pentru ziua respectivă
        if (orarRepository.findByZiua(orarDTO.getZiua()).isPresent()) {
            throw new OrarAlreadyExistsException("Orarul există deja pentru ziua: " + orarDTO.getZiua());
        }

        // Creează entitatea Orar
        Orar orar = new Orar();
        orar.setZiua(orarDTO.getZiua());
        orar.setOraInceput(orarDTO.getOraInceput());
        orar.setOraSfarsit(orarDTO.getOraSfarsit());

        // Salvează Orarul pentru a genera ID-ul
        Orar savedOrar = orarRepository.save(orar);

        // Adaugă orele asociate
        List<OraDTO> oreDTO = orarDTO.getOre();
        if (oreDTO != null) {
            for (OraDTO oraDTO : oreDTO) {
                oraDTO.setOrarId(savedOrar.getId());

                // Verifică dacă ora există deja în orar
                boolean oraExists = oraService.existsOra(oraDTO);
                if (oraExists) {
                    throw new OraAlreadyExistsException("Ora există deja în orar pentru combinația specificată.");
                }

                // Verifică disponibilitatea intervalului orar
                boolean intervalAvailable = oraService.isIntervalAvailable(oraDTO, orarDTO.getZiua());
                if (!intervalAvailable) {
                    throw new IntervalNotAvailableException("Intervalul orar nu este disponibil pentru ora specificată.");
                }

                // Adaugă ora
                oraService.add(oraDTO);
            }
        }

        return savedOrar;
    }

    public Orar updateOrar(Integer id, OrarDTO orarDTO) throws Exception {
        Orar orarExistenta = orarRepository.findById(id)
                .orElseThrow(() -> new OrarNotFoundException("Orarul nu a fost găsit cu ID-ul: " + id));

        // Dacă ziua a fost schimbată, verifică dacă există deja un orar pentru noua zi
        if (!orarExistenta.getZiua().equals(orarDTO.getZiua())) {
            if (orarRepository.findByZiua(orarDTO.getZiua()).isPresent()) {
                throw new OrarAlreadyExistsException("Orarul există deja pentru ziua: " + orarDTO.getZiua());
            }
        }

        // Actualizează Orarul
        orarExistenta.setZiua(orarDTO.getZiua());
        orarExistenta.setOraInceput(orarDTO.getOraInceput());
        orarExistenta.setOraSfarsit(orarDTO.getOraSfarsit());

        Orar savedOrar = orarRepository.save(orarExistenta);

        // Gestionarea Orelor Asociate
        List<OraDTO> oreDTO = orarDTO.getOre();
        if (oreDTO != null) {
            // Șterge orele existente pentru acest orar
            List<Ora> oreExistente = oraService.getOreByOrarId(id);
            for (Ora ora : oreExistente) {
                oraService.deleteOra(ora.getId());
            }

            // Adaugă noile ore
            for (OraDTO oraDTO : oreDTO) {
                oraDTO.setOrarId(savedOrar.getId());

                // Verifică dacă ora există deja în orar
                boolean oraExists = oraService.existsOra(oraDTO);
                if (oraExists) {
                    throw new OraAlreadyExistsException("Ora există deja în orar pentru combinația specificată.");
                }

                // Verifică disponibilitatea intervalului orar
                boolean intervalAvailable = oraService.isIntervalAvailable(oraDTO, orarDTO.getZiua());
                if (!intervalAvailable) {
                    throw new IntervalNotAvailableException("Intervalul orar nu este disponibil pentru ora specificată.");
                }

                // Adaugă ora
                oraService.add(oraDTO);
            }
        }

        return savedOrar;
    }

    public void deleteOrar(Integer id) throws OrarNotFoundException, OraNotFoundException {
        Orar orar = orarRepository.findById(id)
                .orElseThrow(() -> new OrarNotFoundException("Orarul nu a fost găsit cu ID-ul: " + id));

        // Șterge orele asociate cu acest orar
        List<Ora> ore = oraService.getOreByOrarId(id);
        for (Ora oraItem : ore) {
            oraService.deleteOra(oraItem.getId());
        }

        // Șterge Orarul
        orarRepository.delete(orar);
    }
}
