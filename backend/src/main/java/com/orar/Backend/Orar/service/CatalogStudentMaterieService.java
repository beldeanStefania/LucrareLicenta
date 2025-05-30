package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import com.orar.Backend.Orar.enums.MaterieStatus;
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
import java.util.Optional;

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
                    catalogDTO.setSemestru(catalog.getMaterie().getSemestru());
                    catalogDTO.setStudentCod(catalog.getStudent().getCod());
                    catalogDTO.setCodMaterie(catalog.getMaterie().getCod());
                    return catalogDTO;
                })
                .toList();
    }

    public List<CatalogStudentMaterieDTO> getNoteByStudent(String studentCod) {
        List<CatalogStudentMaterie> catalogList = catalogStudentMaterieRepository.findByStudentCod(studentCod);

        return catalogList.stream()
                .map(catalog -> {
                    CatalogStudentMaterieDTO catalogDTO = new CatalogStudentMaterieDTO();
                    catalogDTO.setNota(catalog.getNota());
                    catalogDTO.setSemestru(catalog.getMaterie().getSemestru());

                    catalogDTO.setStudentCod(catalog.getStudent().getCod());
                    catalogDTO.setCodMaterie(catalog.getMaterie().getCod());
                    catalogDTO.setNumeMaterie(catalog.getMaterie().getNume());
                    catalogDTO.setCredite(catalog.getMaterie().getCredite());
                    return catalogDTO;
                })
                .toList();
    }


    public CatalogStudentMaterie addOrUpdate(CatalogStudentMaterieDTO catalogStudentMaterieDTO)
            throws StudentNotFoundException, MaterieNotFoundException {

        if (catalogStudentMaterieDTO.getStudentCod() == null || catalogStudentMaterieDTO.getStudentCod().isEmpty()) {
            throw new IllegalArgumentException("Codul studentului este obligatoriu!");
        }

        if (catalogStudentMaterieDTO.getNumeMaterie() == null || catalogStudentMaterieDTO.getNumeMaterie().isEmpty()) {
            throw new IllegalArgumentException("Numele materiei este obligatoriu!");
        }

        if (catalogStudentMaterieDTO.getNota() == null) {
            throw new IllegalArgumentException("Nota este obligatorie!");
        }

        Student student = studentRepository.findByCod(catalogStudentMaterieDTO.getStudentCod())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with Cod: " + catalogStudentMaterieDTO.getStudentCod()));

        Materie materie = materieRepository.findByNume(catalogStudentMaterieDTO.getNumeMaterie())
                .orElseThrow(() -> new MaterieNotFoundException("Materie not found with Name: " + catalogStudentMaterieDTO.getNumeMaterie()));

        Optional<CatalogStudentMaterie> existingEntry = catalogStudentMaterieRepository.findByStudentCodAndMaterieCod(student.getCod(), materie.getCod());

        if (existingEntry.isPresent()) {
            CatalogStudentMaterie catalog = existingEntry.get();
            catalog.setNota(catalogStudentMaterieDTO.getNota());
            return catalogStudentMaterieRepository.save(catalog);
        } else {
            CatalogStudentMaterie catalog = new CatalogStudentMaterie();
            catalog.setNota(catalogStudentMaterieDTO.getNota());
            catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
            catalog.setStatus(MaterieStatus.ACTIV);
            catalog.setStudent(student);
            catalog.setMaterie(materie);
            return catalogStudentMaterieRepository.save(catalog);
        }
    }

    public CatalogStudentMaterie update(String studentCod, String materieCod, CatalogStudentMaterieDTO catalogStudentMaterieDTO) throws CatalogStudentMaterieNotFoundException, StudentNotFoundException, MaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findByStudentCodAndMaterieCod(studentCod, materieCod)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found for Student Cod: " + studentCod + " and Materie Cod: " + materieCod));

        catalog.setNota(catalogStudentMaterieDTO.getNota());
        catalog.setSemestru(catalogStudentMaterieDTO.getSemestru());
        return catalogStudentMaterieRepository.save(catalog);
    }


    public void delete(Integer id) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findById(id)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found with ID: " + id));
        catalogStudentMaterieRepository.delete(catalog);
    }

    public void deleteByStudentAndMaterie(String studentCod, String materieCod) throws CatalogStudentMaterieNotFoundException {
        CatalogStudentMaterie catalog = catalogStudentMaterieRepository.findByStudentCodAndMaterieCod(studentCod, materieCod)
                .orElseThrow(() -> new CatalogStudentMaterieNotFoundException("Catalog entry not found for Student Cod: " + studentCod + " and Materie Cod: " + materieCod));
        catalogStudentMaterieRepository.delete(catalog);
    }
}
