package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OraDTO;
import com.orar.Backend.Orar.exception.*;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OraService {

    @Autowired
    private OraRepository oraRepository;

    @Autowired
    private OrarRepository orarRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private MaterieRepository materieRepository;

    @Autowired
    private SalaRepository salaRepository;

    public List<Ora> getAll() {
        return oraRepository.findAll();
    }

    public List<Ora> getOreByOrarId(Integer orarId) {
        return oraRepository.findByOrar_Id(orarId);
    }

    public Ora getOraById(Integer id) throws OraNotFoundException {
        return oraRepository.findById(id)
                .orElseThrow(() -> new OraNotFoundException("Ora cu id-ul " + id + " nu a fost găsită"));
    }

    public Ora add(OraDTO oraDTO) throws OrarNotFoundException, MaterieNotFoundException, SalaNotFoundException, SalaNotAvailableException, ProfesorNotAvailableException, OraAlreadyExistsException, ProfesorNotFoundException {

        Orar orar = orarRepository.findById(oraDTO.getOrarId())
                .orElseThrow(() -> new OrarNotFoundException("Orarul cu id-ul " + oraDTO.getOrarId() + " nu a fost găsit"));

        Profesor profesor = profesorRepository.findById(oraDTO.getProfesorId())
                .orElseThrow(() -> new ProfesorNotFoundException("Profesorul cu id-ul " + oraDTO.getProfesorId() + " nu a fost găsit"));

        Materie materie = materieRepository.findById(oraDTO.getMaterieId())
                .orElseThrow(() -> new MaterieNotFoundException("Materia cu id-ul " + oraDTO.getMaterieId() + " nu a fost găsită"));

        Sala sala = salaRepository.findById(oraDTO.getSalaId())
                .orElseThrow(() -> new SalaNotFoundException("Sala cu id-ul " + oraDTO.getSalaId() + " nu a fost găsită"));

        TipOra tipOra = oraDTO.getTip();

        // Verificăm dacă Ora există deja
        Optional<Ora> existingOra = oraRepository.findByOrar_IdAndSala_IdAndProfesor_IdAndMaterie_IdAndTip(
                orar.getId(), sala.getId(), profesor.getId(), materie.getId(), tipOra);
//        if (existingOra.isPresent()) {
//            throw new OraAlreadyExistsException("Ora deja există pentru combinația specificată.");
//        }

        // Verificăm disponibilitatea sălii
        List<Ora> oreInSala = oraRepository.findBySala_IdAndOrar_Ziua(sala.getId(), orar.getZiua());
//        if (!oreInSala.isEmpty()) {
//            throw new SalaNotAvailableException("Sala este deja ocupată în ziua specificată.");
//        }

        // Verificăm disponibilitatea profesorului
        List<Ora> oreCuProfesor = oraRepository.findByProfesor_IdAndOrar_Ziua(profesor.getId(), orar.getZiua());
//        if (!oreCuProfesor.isEmpty()) {
//            throw new ProfesorNotAvailableException("Profesorul este deja ocupat în ziua specificată.");
//        }

        // Creăm entitatea Ora
        Ora ora = new Ora();
        ora.setTip(tipOra);
        ora.setOrar(orar);
        ora.setSala(sala);
        ora.setProfesor(profesor);
        ora.setMaterie(materie);

        return oraRepository.save(ora);
    }


    public Ora update(Integer id, OraDTO oraDTO) throws Exception {

        Ora oraExistenta = oraRepository.findById(id)
                .orElseThrow(() -> new OraNotFoundException("Ora nu a fost găsită cu ID-ul: " + id));

        Orar orar = orarRepository.findById(oraDTO.getOrarId())
                .orElseThrow(() -> new OrarNotFoundException("Orarul nu a fost găsit cu ID-ul: " + oraDTO.getOrarId()));

        Sala sala = salaRepository.findById(oraDTO.getSalaId())
                .orElseThrow(() -> new SalaNotFoundException("Sala nu a fost găsită cu ID-ul: " + oraDTO.getSalaId()));

        Profesor profesor = profesorRepository.findById(oraDTO.getProfesorId())
                .orElseThrow(() -> new ProfesorNotFoundException("Profesorul nu a fost găsit cu ID-ul: " + oraDTO.getProfesorId()));

        Materie materie = materieRepository.findById(oraDTO.getMaterieId())
                .orElseThrow(() -> new MaterieNotFoundException("Materia nu a fost găsită cu ID-ul: " + oraDTO.getMaterieId()));

        TipOra tipOra = oraDTO.getTip();

        // Verificăm dacă altă Ora există cu aceeași combinație
        Optional<Ora> existingOra = oraRepository.findByOrar_IdAndSala_IdAndProfesor_IdAndMaterie_IdAndTip(
                orar.getId(), sala.getId(), profesor.getId(), materie.getId(), tipOra);
        if (existingOra.isPresent() && !existingOra.get().getId().equals(oraExistenta.getId())) {
            throw new OraAlreadyExistsException("O altă Ora există pentru combinația specificată.");
        }

        // Verificăm disponibilitatea sălii (excluzând Ora curentă)
        List<Ora> oreInSala = oraRepository.findBySala_IdAndOrar_Ziua(sala.getId(), orar.getZiua()).stream()
                .filter(o -> !o.getId().equals(oraExistenta.getId()))
                .collect(Collectors.toList());
        if (!oreInSala.isEmpty()) {
            throw new SalaNotAvailableException("Sala este deja ocupată în ziua specificată.");
        }

        // Verificăm disponibilitatea profesorului (excluzând Ora curentă)
        List<Ora> oreCuProfesor = oraRepository.findByProfesor_IdAndOrar_Ziua(profesor.getId(), orar.getZiua()).stream()
                .filter(o -> !o.getId().equals(oraExistenta.getId()))
                .collect(Collectors.toList());
        if (!oreCuProfesor.isEmpty()) {
            throw new ProfesorNotAvailableException("Profesorul este deja ocupat în ziua specificată.");
        }

        // Actualizăm entitatea Ora
        oraExistenta.setTip(tipOra);
        oraExistenta.setOrar(orar);
        oraExistenta.setSala(sala);
        oraExistenta.setProfesor(profesor);
        oraExistenta.setMaterie(materie);

        return oraRepository.save(oraExistenta);
    }

    public boolean existsOra(OraDTO oraDTO) {
        return oraRepository.findByOrar_IdAndSala_IdAndProfesor_IdAndMaterie_IdAndTip(
                oraDTO.getOrarId(), oraDTO.getSalaId(), oraDTO.getProfesorId(), oraDTO.getMaterieId(), oraDTO.getTip()
        ).isPresent();
    }

    public boolean isIntervalAvailable(OraDTO oraDTO, String ziua) {
        // Verifică dacă sala este disponibilă în intervalul specificat
        List<Ora> oreInSala = oraRepository.findBySala_IdAndOrar_Ziua(oraDTO.getSalaId(), ziua);
        if (!oreInSala.isEmpty()) {
            return false;
        }

        // Verifică dacă profesorul este disponibil în intervalul specificat
        List<Ora> oreCuProfesor = oraRepository.findByProfesor_IdAndOrar_Ziua(oraDTO.getProfesorId(), ziua);
        if (!oreCuProfesor.isEmpty()) {
            return false;
        }

        return true;
    }

    public void deleteOra(Integer id) throws OraNotFoundException {
        // Caută Ora după id
        Ora ora = oraRepository.findById(id)
                .orElseThrow(() -> new OraNotFoundException("Ora nu a fost găsită cu ID-ul: " + id));

        // Șterge Ora
        oraRepository.delete(ora);
    }

    public void deleteOra(OraDTO oraDTO) throws OraNotFoundException {
        // Găsește Ora pe baza combinației de atribute
        Optional<Ora> optionalOra = oraRepository.findByOrar_IdAndSala_IdAndProfesor_IdAndMaterie_IdAndTip(
                oraDTO.getOrarId(), oraDTO.getSalaId(), oraDTO.getProfesorId(), oraDTO.getMaterieId(), oraDTO.getTip()
        );

        if (!optionalOra.isPresent()) {
            throw new OraNotFoundException("Ora nu a fost găsită pentru combinația specificată.");
        }

        Ora ora = optionalOra.get();
        oraRepository.delete(ora);
    }

//    public Ora update(OraDTO oraDTO) throws OrarNotFoundException, SalaNotFoundException, ProfesorDoesNotExistException, MaterieNotFoundException, SalaNotAvailableException, ProfesorNotAvailableException, OraAlreadyExistsException, OraNotFoundException, OrarAlreadyExistsException {
//
//        Optional<Ora> oraGasita = oraRepository.findBySalaIdAndProfesorIdAndMaterieIdAndTipOra(oraDTO.getSalaId(), oraDTO.getProfesorId(), oraDTO.getMaterieId(), oraDTO.getTip());
//
//        if(!oraGasita.isPresent()) {
//            throw new OraNotFoundException("Ora not found with ID: " + oraDTO.getSalaId());
//        }
//
//        Ora oraExistenta = oraGasita.get();
//
//        // Verifică existența Orar
//        Orar orar = orarRepository.findFirst()
//                .orElseThrow(() -> new OrarNotFoundException("Orar not found"));
//
//        // Verifică existența Sala
//        Sala sala = salaRepository.findById(oraDTO.getSalaId())
//                .orElseThrow(() -> new SalaNotFoundException("Sala not found with ID: " + oraDTO.getSalaId()));
//
//        // Verifică existența Profesorului
//        Profesor profesor = profesorRepository.findById(oraDTO.getProfesorId())
//                .orElseThrow(() -> new ProfesorDoesNotExistException("Profesor not found with ID: " + oraDTO.getProfesorId()));
//
//        // Verifică existența Materiei
//        Materie materie = materieRepository.findById(oraDTO.getMaterieId())
//                .orElseThrow(() -> new MaterieNotFoundException("Materie not found with ID: " + oraDTO.getMaterieId()));
//
//        TipOra tipOra = oraDTO.getTip();
//        // Verifică disponibilitatea Sălii
//        LocalTime oraInceput = orar.getOraInceput();
//        LocalTime oraSfarsit = orar.getOraSfarsit();
//
//        // Verifică dacă ora există deja pentru combinația specificată (excluzând ora actuală)
//        Optional<Ora> existingOra = oraRepository.findBySalaIdAndProfesorIdAndMaterieIdAndTipOra(
//                sala.getId(), profesor.getId(), materie.getId(), tipOra);
//        if (existingOra.isPresent() && !existingOra.get().getId().equals(oraExistenta.getId())) {
//            throw new OrarAlreadyExistsException("Another Ora exists for the specified combination.");
//        }
//
//        // Verifică disponibilitatea sălii (excluzând ora actuală)
//        List<Ora> oreInSala = oraRepository.findAll().stream()
//                .filter(o -> o.getSala().getId().equals(sala.getId()) && o.getOrar().getZiua().equals(orar.getZiua()) && !o.getId().equals(oraExistenta.getId()))
//                .toList();
//        if (!oreInSala.isEmpty()) {
//            throw new SalaNotAvailableException("Sala is already booked for the specified day.");
//        }
//
//        // Verifică disponibilitatea profesorului (excluzând ora actuală)
//        List<Ora> oreCuProfesor = oraRepository.findAll().stream()
//                .filter(o -> o.getProfesor().getId().equals(profesor.getId()) && o.getOrar().getZiua().equals(orar.getZiua()) && !o.getId().equals(oraExistenta.getId()))
//                .toList();
//        if (!oreCuProfesor.isEmpty()) {
//            throw new ProfesorNotAvailableException("Profesor is already booked for the specified day.");
//        }
//
//        // Actualizează entitatea Ora
//        oraExistenta.setTip(tipOra);
//        oraExistenta.setOrar(orar);
//        oraExistenta.setSala(sala);
//        oraExistenta.setProfesor(profesor);
//        oraExistenta.setMaterie(materie);
//
//        return oraRepository.save(oraExistenta);
//    }
//
//
//    public void deleteOra(OraDTO oraDTO) throws OraNotFoundException {
//        // Identifică Ora pe baza combinației de atribute
//        Optional<Ora> optionalOra = oraRepository.findBySala_IdAndProfesor_IdAndMaterie_IdAndTip(
//                oraDTO.getSalaId(), oraDTO.getProfesorId(), oraDTO.getMaterieId(), oraDTO.getTip());
//
//        if (!optionalOra.isPresent()) {
//            throw new OraNotFoundException("Ora not found with the specified attributes.");
//        }
//
//        Ora ora = optionalOra.get();
//        oraRepository.delete(ora);
//    }
}
