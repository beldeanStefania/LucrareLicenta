package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.CurriculumEntryRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.orar.Backend.Orar.enums.Tip.OBLIGATORIE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CurriculumEntryRepository curriculumEntryRepository;

    @InjectMocks
    private CurriculumService curriculumService;

    private Student student;

    @BeforeEach
    void setup() {
        // prepare a student with specializare id 42
        Specializare specializare = new Specializare();
        specializare.setId(42);
        student = new Student();
        student.setCod("S123");
        student.setSpecializare(specializare);
    }

    @Test
    void getMateriiObligatoriiByYear_returnsDtoList() {
        // given
        when(studentRepository.findByCod("S123")).thenReturn(Optional.of(student));

        // prepare two curriculum entries
        Materie m1 = new Materie(); m1.setNume("Math"); m1.setCod("M001"); m1.setCredite(5);
        CurriculumEntry e1 = new CurriculumEntry();
        e1.setMaterie(m1);
        e1.setSemestru(1);
        e1.setAn(1);
        e1.setSpecializare(student.getSpecializare());
        e1.setTip(OBLIGATORIE);

        Materie m2 = new Materie(); m2.setNume("Physics"); m2.setCod("P001"); m2.setCredite(4);
        CurriculumEntry e2 = new CurriculumEntry();
        e2.setMaterie(m2);
        e2.setSemestru(2);
        e2.setAn(1);
        e2.setSpecializare(student.getSpecializare());
        e2.setTip(OBLIGATORIE);

        when(curriculumEntryRepository.findBySpecializareIdAndAnAndTip(42, 1, OBLIGATORIE))
                .thenReturn(List.of(e1, e2));

        // when
        List<MaterieDTO> result = curriculumService.getMateriiObligatoriiByYear("S123", 1);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(MaterieDTO::getNume)
                .containsExactlyInAnyOrder("Math", "Physics");
    }

    @Test
    void getMateriiObligatoriiByYear_studentNotFound_throws() {
        // given
        when(studentRepository.findByCod("X999")).thenReturn(Optional.empty());

        // when / then
        assertThrows(RuntimeException.class,
                () -> curriculumService.getMateriiObligatoriiByYear("X999", 2));
    }
}
