package com.orar.Backend.Orar.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.MaterieAlreadyExistsException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.MaterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        int sem = materieDTO.getSemestru();
        int an;
        if (sem == 1 || sem == 2) {
            an = 1;
        } else if (sem == 3 || sem == 4) {
            an = 2;
        } else {
            an = 3;
        }
        materie.setAn(an);
        materie.setCredite(materieDTO.getCredite());
        return materie;
    }

    public Materie update(final MaterieDTO materieDTO) throws MaterieNotFoundException, MaterieAlreadyExistsException {
        var materie = findMaterie(materieDTO);
        materie.setNume(materieDTO.getNume());
        materie.setSemestru(materieDTO.getSemestru());
        materie.setCod(materieDTO.getCod());
        materie.setId(materie.getId());
        return materieRepository.save(materie);
    }

    private Materie findMaterie(final MaterieDTO materieDTO) throws MaterieNotFoundException {
        return materieRepository.findByNumeAndCod(materieDTO.getNume(), materieDTO.getCod())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
    }

    public void delete(final int id) throws MaterieNotFoundException {
        var materie = materieRepository.findById(id)
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found"));
        materieRepository.delete(materie);
    }

    public List<ImportResultDTO> importFromCsv(MultipartFile file) {
        List<ImportResultDTO> results = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext(); // cod,nume,prenume,an,grupa,specializare,username,password,email
            String[] line;
            int row = 1;
            while ((line = reader.readNext()) != null) {
                row++;
                try {
                    MaterieDTO dto = new MaterieDTO();
                    dto.setNume(line[0]);
                    dto.setSemestru(Integer.valueOf(line[1]));
                    dto.setCod(line[2]);
                    dto.setCredite(Integer.valueOf(line[3]));
                    this.add(dto);
                    results.add(new ImportResultDTO(row, true, "OK"));
                } catch (Exception ex) {
                    results.add(new ImportResultDTO(row, false, ex.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Nu am putut citi fișierul: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
