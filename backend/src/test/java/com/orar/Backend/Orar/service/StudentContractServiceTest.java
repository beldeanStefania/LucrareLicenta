package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.enums.MaterieStatus;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.exception.ValidationException;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.springframework.web.server.ResponseStatusException;

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
    private Materie materie1, materie2, materie3;
    private CurriculumEntry curriculumEntry1, curriculumEntry2;
    private CatalogStudentMaterie catalogEntry1, catalogEntry2;

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

        // Setup test materials
        materie1 = new Materie();
        materie1.setId(1);
        materie1.setCod("MAT01");
        materie1.setNume("Matematica");
        materie1.setCredite(6);
        materie1.setSemestru(3);

        materie2 = new Materie();
        materie2.setId(2);
        materie2.setCod("FIZ01");
        materie2.setNume("Fizica");
        materie2.setCredite(5);
        materie2.setSemestru(4);

        materie3 = new Materie();
        materie3.setId(3);
        materie3.setCod("CHI01");
        materie3.setNume("Chimie");
        materie3.setCredite(4);
        materie3.setSemestru(3);

        // Setup curriculum entries
        curriculumEntry1 = new CurriculumEntry();
        curriculumEntry1.setMaterie(materie1);
        curriculumEntry1.setTip(Tip.OBLIGATORIE);
        curriculumEntry1.setAn(2);
        curriculumEntry1.setSemestru(3);
        curriculumEntry1.setSpecializare(specializare);

        curriculumEntry2 = new CurriculumEntry();
        curriculumEntry2.setMaterie(materie2);
        curriculumEntry2.setTip(Tip.OPTIONALA);
        curriculumEntry2.setAn(2);
        curriculumEntry2.setSemestru(4);
        curriculumEntry2.setSpecializare(specializare);

        // Setup catalog entries
        catalogEntry1 = new CatalogStudentMaterie();
        catalogEntry1.setStudent(student);
        catalogEntry1.setMaterie(materie1);
        catalogEntry1.setStatus(MaterieStatus.ACTIV);
        catalogEntry1.setSemestru(3);

        catalogEntry2 = new CatalogStudentMaterie();
        catalogEntry2.setStudent(student);
        catalogEntry2.setMaterie(materie2);
        catalogEntry2.setStatus(MaterieStatus.FINALIZATA);
        catalogEntry2.setSemestru(4);
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

    void testGenerateContractFromSelection_Valid() throws Exception {
                when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));


        Materie sem1 = new Materie();
                sem1.setId(1);
                sem1.setCod("SEM1");
                sem1.setNume("Curs Sem1");
                sem1.setCredite(30);
                sem1.setSemestru(3);   // 3 % 2 == 1 → semestru I relativ

        Materie sem2 = new Materie();
                sem2.setId(2);
                sem2.setCod("SEM2");
                sem2.setNume("Curs Sem2");
                sem2.setCredite(30);
                sem2.setSemestru(4);   // 4 % 2 == 0 → semestru II relativ

        when(materieRepo.findAllByCodIn(List.of("SEM1", "SEM2")))
                          .thenReturn(List.of(sem1, sem2));
                // Nu se fac verificări „future obligatory” pentru aceste coduri:
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1))
                          .thenReturn(Optional.empty());
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 2))
                          .thenReturn(Optional.empty());
                // Nu există intrări active în catalog:
        when(catalogRepo.findByStudentCodAndMaterieCod(any(), any()))
                       .thenReturn(Optional.empty());

                      byte[] result = service.generateContractFromSelection(
                        "123", 2,List.of("SEM1", "SEM2"));
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

    @Test
    void testGetAvailableCoursesForContract_StudentNotFound() {
        when(studentRepo.findByCod("invalid")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getAvailableCoursesForContract("invalid", 2, 1));
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void testGetAvailableCoursesForContract_AnContractTooHigh() {
        student.setAn(2);
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.getAvailableCoursesForContract("123", 3, 1));
        assertTrue(ex.getReason().contains("Nu poți genera contract pentru anul"));
    }

    @Test
    void testGetAvailableCoursesForContract_WithActiveStatusUpdate() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create a catalog entry with ACTIV status and semester < semestruAbsolut
        CatalogStudentMaterie activeEntry = new CatalogStudentMaterie();
        activeEntry.setStatus(MaterieStatus.ACTIV);
        activeEntry.setSemestru(1); // Less than semestruAbsolut (3)
        activeEntry.setMaterie(materie1); // Set the materie

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(activeEntry));
        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of());
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of());

        service.getAvailableCoursesForContract("123", 2, 1);

        verify(catalogRepo).save(activeEntry);
        assertEquals(MaterieStatus.FINALIZATA, activeEntry.getStatus());
    }

    @Test
    void testGetAvailableCoursesForContract_WithExternalObligatory() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Specializare otherSpec = new Specializare();
        otherSpec.setId(2);

        CurriculumEntry externalEntry = new CurriculumEntry();
        externalEntry.setMaterie(materie1);
        externalEntry.setTip(Tip.OBLIGATORIE);
        externalEntry.setSpecializare(otherSpec);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of());
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of(externalEntry));
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(1, result.size());
        assertEquals(Tip.OPTIONALA, result.get(0).getTip()); // External obligatory becomes optional
        assertFalse(result.get(0).isSelected());
    }

    @Test
    void testGetAvailableCoursesForContract_FilterByStatus() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of(curriculumEntry1));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of());

        // Student has ACTIV status for this material
        CatalogStudentMaterie activEntry = new CatalogStudentMaterie();
        activEntry.setMaterie(materie1);
        activEntry.setStatus(MaterieStatus.ACTIV);
        activEntry.setSemestru(3); // Set semestru

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(activEntry));

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(0, result.size()); // Filtered out because of ACTIV status
    }

    @Test
    void testGetAvailableCoursesForContract_WithPicataStatus() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of(curriculumEntry1));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of());

        // Student has PICATA status for this material
        CatalogStudentMaterie picataEntry = new CatalogStudentMaterie();
        picataEntry.setMaterie(materie1);
        picataEntry.setStatus(MaterieStatus.PICATA);

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(picataEntry));

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(1, result.size()); // Not filtered out because PICATA status allows re-enrollment
    }

    @Test
    void testGetAvailableCoursesForContract_WithOptionalPackage() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        MateriiOptionale optPackage = new MateriiOptionale();
        optPackage.setId(1);
        optPackage.setNume("Optional Package");

        curriculumEntry2.setOptionale(optPackage);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 4)).thenReturn(List.of(curriculumEntry2));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 4, Tip.OBLIGATORIE)).thenReturn(List.of());
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 2);

        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getMateriiOptionaleId());
    }

    @Test
    void testGetContractCourses_StudentNotFound() {
        when(studentRepo.findByCod("invalid")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getContractCourses("invalid", 2));
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void testGetContractCourses_WithOptionalPackage() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        MateriiOptionale optPackage = new MateriiOptionale();
        optPackage.setId(1);

        curriculumEntry1.setOptionale(optPackage);

        when(curriculumRepo.findBySpecializareIdAndAn(1, 2)).thenReturn(List.of(curriculumEntry1));

        List<ContractDTO> result = service.getContractCourses("123", 2);

        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getMateriiOptionaleId());
    }

    @Test
    void testGenerateContractPdfWithoutPersist_ObligatoryInFutureYear() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie futureMaterie = new Materie();
        futureMaterie.setId(1);
        futureMaterie.setCod("MAT01");
        futureMaterie.setNume("Future Math");
        futureMaterie.setCredite(6);
        futureMaterie.setSemestru(5);

        CurriculumEntry futureEntry = new CurriculumEntry();
        futureEntry.setTip(Tip.OBLIGATORIE);
        futureEntry.setAn(3); // Future year for student in year 2

        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(futureMaterie));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.of(futureEntry));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Nu poți alege materia"));
    }

    @Test
    void testGenerateContractFromSelection_StudentNotFound() {
        when(studentRepo.findByCod("invalid")).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.generateContractFromSelection("invalid", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Studentul nu a fost găsit"));
    }

    @Test
    void testGenerateContractFromSelection_EmptyMaterii() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));
        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of());

        Exception ex = assertThrows(Exception.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Nu au fost găsite materiile"));
    }

    @Test
    void testGenerateContractFromSelection_FutureObligatoryMaterie() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie futureMaterie = new Materie();
        futureMaterie.setId(1);
        futureMaterie.setCod("MAT01");
        futureMaterie.setNume("Future Math");

        CurriculumEntry futureEntry = new CurriculumEntry();
        futureEntry.setTip(Tip.OBLIGATORIE);
        futureEntry.setAn(3);

        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(futureMaterie));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.of(futureEntry));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("Nu poți alege materia"));
    }

    @Test
    void testGenerateContractFromSelection_ActiveMaterie() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(materie1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());

        CatalogStudentMaterie activeEntry = new CatalogStudentMaterie();
        activeEntry.setStatus(MaterieStatus.ACTIV);
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "MAT01")).thenReturn(Optional.of(activeEntry));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("nu pot fi adăugate din nou în contract"));
    }

    @Test
    void testGenerateContractFromSelection_FinalizataMaterie() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(materie1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());

        CatalogStudentMaterie finalizataEntry = new CatalogStudentMaterie();
        finalizataEntry.setStatus(MaterieStatus.FINALIZATA);
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "MAT01")).thenReturn(Optional.of(finalizataEntry));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01")));
        assertTrue(ex.getMessage().contains("sunt deja active sau finalizate"));
    }

    @Test
    void testGenerateContractFromSelection_OptionalPackageNotFound() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        MateriiOptionale opt = new MateriiOptionale();
        opt.setId(999);

        Materie m1 = new Materie();
        m1.setId(1);
        m1.setCod("MAT01");

        Materie m2 = new Materie();
        m2.setId(2);
        m2.setCod("MAT02");

        CurriculumEntry entry = new CurriculumEntry();
        entry.setOptionale(opt);

        when(materieRepo.findAllByCodIn(List.of("MAT01", "MAT02"))).thenReturn(List.of(m1, m2));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.of(entry));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 2)).thenReturn(Optional.of(entry));
        when(catalogRepo.findByStudentCodAndMaterieCod(anyString(), anyString())).thenReturn(Optional.empty());
        when(optionaleRepo.findById(999)).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT01", "MAT02")));
        assertTrue(ex.getMessage().contains("acest pachet"));
    }

    @Test
    void testServiceConstructor() {
        StudentContractService service = new StudentContractService();
        assertNotNull(service);
    }
}
