package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieAlreadyExistsException;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieNotFoundException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogStudentMaterieService {

    @Autowired
    private CatalogStudentMaterieRepository catalogStudentMaterieRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MaterieRepository materieRepository;

    public List<CatalogStudentMaterie> getAll() {
        return catalogStudentMaterieRepository.findAll();
    }


    public CatalogStudentMaterie add(CatalogStudentMaterieDTO catalogStudentMaterieDTO) throws CatalogStudentMaterieAlreadyExistsException, StudentNotFoundException, MaterieNotFoundException {
        // Verifică dacă studentul există
        Student student = studentRepository.findById(catalogStudentMaterieDTO.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + catalogStudentMaterieDTO.getStudentId()));
        // Verifică dacă materie există
        Materie materie = materieRepository.findById(catalogStudentMaterieDTO.getMaterieId())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found with ID: " + catalogStudentMaterieDTO.getMaterieId()));

        // Creează entitatea
        CatalogStudentMaterie catalog = new CatalogStudentMaterie();
        catalog.setNota(catalogStudentMaterieDTO.getNota());
        catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
        catalog.setStudent(student);
        catalog.setMaterie(materie);
        return catalogStudentMaterieRepository.save(catalog);
    }

    // Actualizează o înregistrare existentă
    public CatalogStudentMaterie update(Integer id, CatalogStudentMaterieDTO catalogStudentMaterieDTO) throws CatalogStudentMaterieNotFoundException, StudentNotFoundException, MaterieNotFoundException {
        // Găsește înregistrarea existentă
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
        // Verifică și actualizează studentul dacă este necesar
        if (catalogStudentMaterieDTO.getStudentId() != null && !catalog.getStudent().getId().equals(catalogStudentMaterieDTO.getStudentId())) {
            Student student = studentRepository.findById(catalogStudentMaterieDTO.getStudentId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + catalogStudentMaterieDTO.getStudentId()));
            catalog.setStudent(student);
        }
        // Verifică și actualizează materia dacă este necesar
        if (catalogStudentMaterieDTO.getMaterieId() != null && !catalog.getMaterie().getId().equals(catalogStudentMaterieDTO.getMaterieId())) {
            Materie materie = materieRepository.findById(catalogStudentMaterieDTO.getMaterieId())
                    .orElseThrow(() -> new MaterieNotFoundException("Materie not found with ID: " + catalogStudentMaterieDTO.getMaterieId()));
            catalog.setMaterie(materie);
        }
        // Actualizează nota și semestru
        catalog.setNota(catalogStudentMaterieDTO.getNota());
        catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
        return catalogStudentMaterieRepository.save(catalog);
    }

    // Șterge o înregistrare după ID
    public void delete(Integer id) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
        catalogStudentMaterieRepository.delete(catalog);
    }

    // Șterge o înregistrare după nume și prenume al studentului și ID-ul materiei
    public void deleteByStudentAndMaterie(String numeStudent, String prenumeStudent, Integer materieId) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findByStudentNumeAndStudentPrenumeAndMaterieId(numeStudent, prenumeStudent, materieId)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found for Student: " + numeStudent + " " + prenumeStudent + " and Materie ID: " + materieId));
        catalogStudentMaterieRepository.delete(catalog);
    }

    // Obține o înregistrare după ID
    public CatalogStudentMaterie getById(Integer id) throws CatalogStudentMaterieNotFoundException {
        return catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
    }

//    public List<CatalogStudentMaterie> getAll() {
//        return catalogStudentMaterieRepository.findAll();
//    }
//
//    public CatalogStudentMaterie add(final CatalogStudentMaterie catalogStudentMaterie) {
//        return catalogStudentMaterieRepository.save(catalogStudentMaterie);
//    }
//
//    public CatalogStudentMaterie update(final CatalogStudentMaterie catalogStudentMaterie) {
//        return catalogStudentMaterieRepository.save(catalogStudentMaterie);
//    }
//
//    public void delete(final CatalogStudentMaterie catalogStudentMaterie) {
//        catalogStudentMaterieRepository.delete(catalogStudentMaterie);
//    }
}
