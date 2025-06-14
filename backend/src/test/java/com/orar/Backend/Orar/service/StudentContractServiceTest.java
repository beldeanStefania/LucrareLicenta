package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.exception.ValidationException;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentContractServiceTest {

    @InjectMocks
    private StudentContractService service;

    @Mock
    private StudentRepository studentRepo;
    @Mock
    private CatalogStudentMaterieRepository catalogRepo;
    @Mock
    private MaterieRepository materieRepo;
    @Mock
    private CurriculumEntryRepository curriculumRepo;
    @Mock
    private ContractRepository contractRepo;
    @Mock
    private MateriiOptionaleRepository optionaleRepo;

    private Student student;
    private Specializare specializare;

    @BeforeEach
    void setup() {
        specializare = new Specializare();
        specializare.setId(1);

        student = new Student();
        student.setCod("123");
        student.setAn(2);
        student.setGrupa("214");
        student.setNume("Ion");
        student.setPrenume("Popescu");
        student.setSpecializare(specializare);
    }

    @Test
    void testGetAvailableCoursesForContract_ValidResult() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        CurriculumEntry entry = new CurriculumEntry();
        Materie materie = new Materie();
        materie.setCod("MAT01");
        materie.setNume("Mate");
        materie.setCredite(6);
        materie.setSemestru(3);
        entry.setMaterie(materie);
        entry.setTip(Tip.OBLIGATORIE);
        entry.setAn(2);
        entry.setSemestru(3);
        entry.setSpecializare(specializare);
        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of(entry));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of(entry));
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(1, result.size());
        assertEquals("MAT01", result.get(0).getCod());
    }

    @Test
    void testGetAvailableCoursesForContract_InvalidAn_Throws() {
        student.setAn(2);
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));
        assertThrows(RuntimeException.class, () -> service.getAvailableCoursesForContract("123", 3, 1));
    }

    @Test
    void testGetContractCourses_Success() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie materie = new Materie();
        materie.setCod("MAT01");
        materie.setNume("Mate");
        materie.setCredite(6);
        materie.setSemestru(3);

        CurriculumEntry entry = new CurriculumEntry();
        entry.setMaterie(materie);
        entry.setTip(Tip.OBLIGATORIE);
        entry.setSemestru(2);

        when(curriculumRepo.findBySpecializareIdAndAn(1, 2)).thenReturn(List.of(entry));

        List<ContractDTO> result = service.getContractCourses("123", 2);
        assertEquals(1, result.size());
        assertEquals("MAT01", result.get(0).getCod());
    }

    @Test
    void testGenerateContractPdfWithoutPersist_Valid() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie m1 = new Materie();
        m1.setCod("MAT01");
        m1.setNume("Mate");
        m1.setCredite(6);
        m1.setSemestru(3);
        m1.setId(1);
        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(m1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());

        byte[] pdf = service.generateContractPdfWithoutPersist("123", 2, List.of("MAT01"));
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void testGenerateContractPdfWithoutPersist_InvalidStudent() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Studentul nu a fost găsit"));
    }

    @Test
    void testGenerateContractPdfWithoutPersist_EmptyMaterii() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));
        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of());

        Exception ex = assertThrows(Exception.class, () -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Nu au fost găsite materiile"));
    }

    @Test
    void testGenerateContractFromSelection_Valid() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie m = new Materie();
        m.setCod("MAT01");
        m.setNume("Mate");
        m.setCredite(6);
        m.setSemestru(3);
        m.setId(1);
        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(m));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "MAT01")).thenReturn(Optional.empty());

        byte[] result = service.generateContractFromSelection("123", 2, List.of("MAT01"));

        assertNotNull(result);
        verify(contractRepo).save(any());
    }

    @Test
    void testGenerateContractFromSelection_OptionalConflict_Throws() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        MateriiOptionale opt = new MateriiOptionale();
        opt.setId(9);
        opt.setNume("Pachet Test");

        Materie m = new Materie();
        m.setCod("MAT01");
        m.setNume("Mate");
        m.setCredite(6);
        m.setSemestru(3);
        m.setId(1);

        CurriculumEntry entry = new CurriculumEntry();
        entry.setMaterie(m);
        entry.setTip(Tip.OPTIONALA);
        entry.setOptionale(opt);

        when(materieRepo.findAllByCodIn(List.of("MAT01", "MAT02"))).thenReturn(List.of(m, m));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.of(entry));
        when(catalogRepo.findByStudentCodAndMaterieCod(any(), any())).thenReturn(Optional.empty());
        when(optionaleRepo.findById(9)).thenReturn(Optional.of(opt));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01", "MAT02")));
        assertTrue(ex.getMessage().contains("Pachet Test"));
    }

    @Test
    void testHasContract_True() {
        when(contractRepo.findByStudentCodAndAnContract("123", 2))
                .thenReturn(Optional.of(new Contract("123", 2)));

        assertTrue(service.hasContract("123", 2));
    }

    @Test
    void testHasContract_False() {
        when(contractRepo.findByStudentCodAndAnContract("123", 2))
                .thenReturn(Optional.empty());

        assertFalse(service.hasContract("123", 2));
    }
}
