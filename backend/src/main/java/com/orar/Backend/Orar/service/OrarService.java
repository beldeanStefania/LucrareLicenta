package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.dto.OrarDetailsDTO;
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

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private MaterieRepository materieRepository;

    public List<Orar> getOrarByGrupa(String grupa) {
        return orarRepository.findByGrupa(grupa);
    }

    public List<OrarDetailsDTO> getOrarDetailsByGrupa(String grupa) {
        List<Orar> orare = orarRepository.findByGrupa(grupa);

        return orare.stream()
                .map(orar -> {
                    String cadruDidactic = orar.getRepartizareProf().getProfesor().getNume() + " " +
                            orar.getRepartizareProf().getProfesor().getPrenume();

                    return OrarDetailsDTO.builder()
                            .zi(orar.getZi())
                            .formatia(orar.getFormatia())
                            .oraInceput(orar.getOraInceput())
                            .oraSfarsit(orar.getOraSfarsit())
                            .grupa(orar.getGrupa())
                            .sala(orar.getSala().getNume())
                            .tipul(orar.getRepartizareProf().getTip())
                            .disciplina(orar.getRepartizareProf().getMaterie().getNume())
                            .cadruDidactic(cadruDidactic)
                            .frecventa(orar.getFrecventa())
                            .build();
                })
                .sorted((a, b) -> compareDays(a.getZi(), b.getZi())) // Adaugă sortarea după zi
                .collect(Collectors.toList());
    }

    private int compareDays(String zi1, String zi2) {
        List<String> zileInOrdine = List.of("Luni", "Marti", "Miercuri", "Joi", "Vineri");
        return Integer.compare(zileInOrdine.indexOf(zi1), zileInOrdine.indexOf(zi2));
    }


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
        Orar newOrar = new Orar();
        newOrar.setGrupa(orarDTO.getGrupa());
        newOrar.setZi(orarDTO.getZi());
        newOrar.setOraInceput(orarDTO.getOraInceput());
        newOrar.setOraSfarsit(orarDTO.getOraSfarsit());

        // Verifică sau creează repartizarea
        RepartizareProf repartizareProf = orarDTO.getRepartizareProfId() != null
                ? repartizareProfRepository.findById(orarDTO.getRepartizareProfId())
                .orElseThrow(() -> new NoSuchElementException("RepartizareProf not found"))
                : createRepartizareProfIfNotExists(orarDTO);

        newOrar.setRepartizareProf(repartizareProf);

        // Asociază sala
        Sala sala = salaRepository.findById(orarDTO.getSalaId())
                .orElseThrow(() -> new NoSuchElementException("Sala not found"));
        newOrar.setSala(sala);

        // Setează frecvența
        newOrar.setFrecventa(orarDTO.getFrecventa() != null ? orarDTO.getFrecventa() : "N/A");

        // Setează formația
        System.out.println("Grupa primită: " + orarDTO.getGrupa());
        System.out.println("Semigrupa primită: " + orarDTO.getSemigrupa());
        System.out.println("Tipul activității: " + orarDTO.getTip());

        newOrar.setFormatia(determineFormatia(orarDTO.getGrupa(), repartizareProf.getTip(), orarDTO.getSemigrupa()));
        System.out.println("Formatia calculată: " + newOrar.getFormatia());

        return orarRepository.save(newOrar);
    }


    private RepartizareProf createRepartizareProfIfNotExists(OrarDTO orarDTO) {
        Profesor profesor = profesorRepository.findById(orarDTO.getProfesorId())
                .orElseThrow(() -> new NoSuchElementException("Profesor not found"));

        Materie materie = materieRepository.findByNume(orarDTO.getMaterie())
                .orElseThrow(() -> new NoSuchElementException("Materie not found"));

        // Verifică dacă repartizarea există deja
        Optional<RepartizareProf> existingRepartizare = repartizareProfRepository.findByProfesorAndMaterieAndTip(
                profesor, materie, orarDTO.getTip());

        // Dacă există, returnează repartizarea găsită
        if (existingRepartizare.isPresent()) {
            return existingRepartizare.get();
        }

        // Dacă nu există, creează o nouă repartizare
        RepartizareProf newRepartizare = new RepartizareProf();
        newRepartizare.setProfesor(profesor);
        newRepartizare.setMaterie(materie);
        newRepartizare.setTip(orarDTO.getTip());
        return repartizareProfRepository.save(newRepartizare);
    }

    private String determineFormatia(String grupa, String tip, String semigrupa) {
        String formatia;
        if ("Curs".equalsIgnoreCase(tip)) {
            formatia = "MIR";
        } else if ("Seminar".equalsIgnoreCase(tip)) {
            formatia = grupa;
        } else if ("Laborator".equalsIgnoreCase(tip)) {
            formatia = semigrupa != null && !semigrupa.isEmpty() ? grupa + "/" + semigrupa : grupa;
        } else {
            throw new IllegalArgumentException("Tip necunoscut: " + tip);
        }
        System.out.println("Determinată formație: " + formatia); // Debugging
        return formatia;
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

    public List<OrarDetailsDTO> getOrarDetailsByProfesor(Integer profesorId) {
        List<Orar> orarList = orarRepository.findByRepartizareProf_Profesor_Id(profesorId);

        return orarList.stream()
                .map(orar -> {
                    OrarDetailsDTO orarDetailsDTO = new OrarDetailsDTO();
                    orarDetailsDTO.setZi(orar.getZi());
                    orarDetailsDTO.setFormatia(orar.getFormatia());
                    orarDetailsDTO.setOraInceput(orar.getOraInceput());
                    orarDetailsDTO.setOraSfarsit(orar.getOraSfarsit());
                    orarDetailsDTO.setGrupa(orar.getGrupa());
                    orarDetailsDTO.setSala(orar.getSala().getNume());
                    orarDetailsDTO.setTipul(orar.getRepartizareProf().getTip());
                    orarDetailsDTO.setFrecventa(orar.getFrecventa());
                    orarDetailsDTO.setDisciplina(orar.getRepartizareProf().getMaterie().getNume());

                    return orarDetailsDTO;
                })
                .sorted((a, b) -> compareDays(a.getZi(), b.getZi()))
                .toList();
    }
}