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

    public List<CatalogStudentMaterieDTO> getAll() {
        List<CatalogStudentMaterie> catalogList = catalogStudentMaterieRepository.findAll();

        return catalogList.stream()
                .map(catalog -> {
                    CatalogStudentMaterieDTO catalogDTO = new CatalogStudentMaterieDTO();
                    catalogDTO.setNota(catalog.getNota());
                    catalogDTO.setSemestru(catalog.getSemestru());
                    catalogDTO.setStudentId(catalog.getStudent().getId());
                    catalogDTO.setCodMaterie(catalog.getMaterie().getCod());
                    return catalogDTO;
                })
                .toList();
        //return catalogStudentMaterieRepository.findAll();
    }


    public CatalogStudentMaterie add(CatalogStudentMaterieDTO catalogStudentMaterieDTO) throws CatalogStudentMaterieAlreadyExistsException, StudentNotFoundException, MaterieNotFoundException {

        Student student = studentRepository.findById(catalogStudentMaterieDTO.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + catalogStudentMaterieDTO.getStudentId()));

        Materie materie = materieRepository.findByCod(catalogStudentMaterieDTO.getCodMaterie())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found with code: " + catalogStudentMaterieDTO.getCodMaterie()));


        CatalogStudentMaterie catalog = new CatalogStudentMaterie();
        catalog.setNota(catalogStudentMaterieDTO.getNota());
        catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
        catalog.setStudent(student);
        catalog.setMaterie(materie);
        return catalogStudentMaterieRepository.save(catalog);
    }


    public CatalogStudentMaterie update(Integer id, CatalogStudentMaterieDTO catalogStudentMaterieDTO) throws CatalogStudentMaterieNotFoundException, StudentNotFoundException, MaterieNotFoundException {

        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
        if (catalogStudentMaterieDTO.getStudentId() != null && !catalog.getStudent().getId().equals(catalogStudentMaterieDTO.getStudentId())) {
            Student student = studentRepository.findById(catalogStudentMaterieDTO.getStudentId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + catalogStudentMaterieDTO.getStudentId()));
            catalog.setStudent(student);
        }

        if (catalogStudentMaterieDTO.getCodMaterie() != null && !catalog.getMaterie().getCod().equals(catalogStudentMaterieDTO.getCodMaterie())) {
            Materie materie = materieRepository.findByCod(catalogStudentMaterieDTO.getCodMaterie())
                    .orElseThrow(() -> new MaterieNotFoundException("Materie not found with ID: " + catalogStudentMaterieDTO.getCodMaterie()));
            catalog.setMaterie(materie);
        }
        catalog.setNota(catalogStudentMaterieDTO.getNota());
        catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
        return catalogStudentMaterieRepository.save(catalog);
    }

    public void delete(Integer id) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
        catalogStudentMaterieRepository.delete(catalog);
    }

    public void deleteByStudentAndMaterie(String numeStudent, String prenumeStudent, Integer materieId) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findByStudentNumeAndStudentPrenumeAndMaterieId(numeStudent, prenumeStudent, materieId)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found for Student: " + numeStudent + " " + prenumeStudent + " and Materie ID: " + materieId));
        catalogStudentMaterieRepository.delete(catalog);
    }

    public CatalogStudentMaterie getById(Integer id) throws CatalogStudentMaterieNotFoundException {
        return catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
    }
}
