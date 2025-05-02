package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CurriculumEntryRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.orar.Backend.Orar.enums.Tip.OBLIGATORIE;

@Service
public class CurriculumService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CurriculumEntryRepository curriculumEntryRepository;

    public List<MaterieDTO> getMateriiObligatoriiByYear(String studentCod, int anContract) {
        Student student = studentRepository.findByCod(studentCod).orElseThrow(() -> new RuntimeException("Student not found"));
        int specializareId = student.getSpecializare().getId();

        List<CurriculumEntry> entries = curriculumEntryRepository.findBySpecializareIdAndAnAndTip(specializareId, anContract, OBLIGATORIE);

        return entries.stream()
                .map(entry -> {
                    var materie = entry.getMaterie();
                    return new MaterieDTO(
                            materie.getNume(),
                            entry.getSemestru(),
                            materie.getCod(),
                            materie.getCredite()
                    );
                })
                .toList();
    }
}
