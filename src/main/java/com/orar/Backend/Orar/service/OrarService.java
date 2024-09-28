//package com.orar.Backend.Orar.service;
//
//import com.orar.Backend.Orar.dto.OraDTO;
//import com.orar.Backend.Orar.dto.OrarDTO;
//import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
//import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
//import com.orar.Backend.Orar.exception.SalaNotFoundException;
//import com.orar.Backend.Orar.model.*;
//import com.orar.Backend.Orar.repository.MaterieRepository;
//import com.orar.Backend.Orar.repository.OrarRepository;
//import com.orar.Backend.Orar.repository.ProfesorRepository;
//import com.orar.Backend.Orar.repository.SalaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class OrarService {
//
//    @Autowired
//    private OrarRepository orarRepository;
//
//    @Autowired
//    private SalaRepository salaRepository;
//
//    @Autowired
//    private MaterieRepository materieRepository;
//
//    @Autowired
//    private ProfesorRepository profesorRepository;
//
//    public boolean isSalaAvailable(Integer salaId, String ziua, LocalTime oraInceput, LocalTime oraSfarsit) {
//        return orarRepository.findOrar(salaId, ziua, oraInceput, oraSfarsit).isEmpty();
//    }
//
//    public Orar scheduleOrar(OrarDTO orarDTO) throws MaterieDoesNotExistException, SalaNotFoundException, ProfesorDoesNotExistException {
//
//        Materie materie = findMaterie(orarDTO.getMateria());
//        Sala sala = findAvailableSala(orarDTO.getZiua(), orarDTO.getOraInceput(), orarDTO.getOraSfarsit());
//        Profesor profesor = findProfesor(orarDTO.);
//
//        Orar orar = buildOrar(orarDTO);
//        Ora ora = new Ora();
//        ora.setOrar(orar);
//        ora.setMaterii(List.of(materie));
//        ora.setProfesor(List.of(profesor));
//        ora.setSala(sala);
//
//        orar.setOra((List.of(ora)));
//        return orarRepository.save(orar);
//    }
//
//    public Profesor findProfesor(Ora ora) throws ProfesorDoesNotExistException {
//        return profesorRepository.findByOra(ora)
//                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesorul nu exista"));
//    }
//
//    public Materie findMaterie(String nume) throws MaterieDoesNotExistException {
//        return materieRepository.findByNume(nume)
//                .orElseThrow(() -> new MaterieDoesNotExistException("Materia cu numele " + nume + " nu exista"));
//    }
//
//    public Sala findSala(String nume) throws SalaNotFoundException {
//        return salaRepository.findByNume(nume)
//                .orElseThrow(() -> new SalaNotFoundException("Sala cu numele " + nume + " nu exista"));
//    }
//
//    public Sala findAvailableSala(String ziua, LocalTime oraInceput, LocalTime oraSfarsit) throws SalaNotFoundException {
//        List<Sala> salaList = salaRepository.findAll();
//        for (Sala sala : salaList) {
//            if (isSalaAvailable(sala.getId(), ziua, oraInceput, oraSfarsit)) {
//                return sala;
//            }
//        }
//        throw new SalaNotFoundException("Nu exista sali disponibile pentru ziua " + ziua + " intre orele " + oraInceput + " si " + oraSfarsit);
//    }
//
//    public Orar buildOrar(OrarDTO orarDTO) {
//        Orar orar = new Orar();
//        orar.setOraInceput(orarDTO.getOraInceput());
//        orar.setOraSfarsit(orarDTO.getOraSfarsit());
//        orar.setZiua(orarDTO.getZiua());
//        return orar;
//    }
//
//}
