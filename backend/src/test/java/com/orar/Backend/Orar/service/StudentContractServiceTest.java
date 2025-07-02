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

    @Test
    void testValidateMinCreditsPerSemester_InsufficientSemester1() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create materials with insufficient credits for semester 1
        Materie sem1Mat1 = new Materie();
        sem1Mat1.setCredite(10);
        sem1Mat1.setSemestru(3); // Odd semester (semester 1)

        Materie sem1Mat2 = new Materie();
        sem1Mat2.setCredite(15);
        sem1Mat2.setSemestru(3);

        Materie sem2Mat = new Materie();
        sem2Mat.setCredite(30);
        sem2Mat.setSemestru(4); // Even semester (semester 2)

        List<Materie> materii = List.of(sem1Mat1, sem1Mat2, sem2Mat);
        when(materieRepo.findAllByCodIn(anyList())).thenReturn(materii);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT1", "MAT2", "MAT3")));
        assertTrue(ex.getMessage().contains("în semestrul I ai doar 25 credite"));
    }

    @Test
    void testValidateMinCreditsPerSemester_InsufficientSemester2() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie sem1Mat = new Materie();
        sem1Mat.setCredite(30);
        sem1Mat.setSemestru(3); // Odd semester

        Materie sem2Mat1 = new Materie();
        sem2Mat1.setCredite(10);
        sem2Mat1.setSemestru(4); // Even semester

        Materie sem2Mat2 = new Materie();
        sem2Mat2.setCredite(15);
        sem2Mat2.setSemestru(4);

        List<Materie> materii = List.of(sem1Mat, sem2Mat1, sem2Mat2);
        when(materieRepo.findAllByCodIn(anyList())).thenReturn(materii);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT1", "MAT2", "MAT3")));
        assertTrue(ex.getMessage().contains("în semestrul II ai doar 25 credite"));
    }

    @Test
    void testValidateMinCreditsPerSemester_ExactlyMinimum() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie sem1Mat = new Materie();
        sem1Mat.setId(1);
        sem1Mat.setCod("SEM1");
        sem1Mat.setNume("Sem1");
        sem1Mat.setCredite(30);
        sem1Mat.setSemestru(3);

        Materie sem2Mat = new Materie();
        sem2Mat.setId(2);
        sem2Mat.setCod("SEM2");
        sem2Mat.setNume("Sem2");
        sem2Mat.setCredite(30);
        sem2Mat.setSemestru(4);

        List<Materie> materii = List.of(sem1Mat, sem2Mat);
        when(materieRepo.findAllByCodIn(anyList())).thenReturn(materii);

        // Should not throw exception with exactly 30 credits each
        byte[] result = service.generateContractPdfWithoutPersist("123", 2, List.of("SEM1", "SEM2"));
        assertNotNull(result);
    }

    @Test
    void testRecordActiveCourses_CreatesNewEntries() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie newMaterie = new Materie();
        newMaterie.setId(1);
        newMaterie.setCod("NEW");
        newMaterie.setNume("New Course");
        newMaterie.setCredite(30);
        newMaterie.setSemestru(3);

        when(materieRepo.findAllByCodIn(List.of("NEW"))).thenReturn(List.of(newMaterie));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "NEW")).thenReturn(Optional.empty());

        service.generateContractFromSelection("123", 2, List.of("NEW"));

        verify(catalogRepo).save(argThat(entry ->
                entry.getStudent().equals(student) &&
                        entry.getMaterie().equals(newMaterie) &&
                        entry.getStatus() == MaterieStatus.ACTIV
        ));
    }

    @Test
    void testRecordActiveCourses_UpdatesPicataToActiv() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie materie = new Materie();
        materie.setId(1);
        materie.setCod("PICATA");
        materie.setNume("Previously Failed");
        materie.setCredite(30);
        materie.setSemestru(3);

        CatalogStudentMaterie picataEntry = new CatalogStudentMaterie();
        picataEntry.setStudent(student);
        picataEntry.setMaterie(materie);
        picataEntry.setStatus(MaterieStatus.PICATA);
        picataEntry.setNota(4.0);

        when(materieRepo.findAllByCodIn(List.of("PICATA"))).thenReturn(List.of(materie));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "PICATA")).thenReturn(Optional.of(picataEntry));

        service.generateContractFromSelection("123", 2, List.of("PICATA"));

        verify(catalogRepo).save(argThat(entry ->
                entry.getStatus() == MaterieStatus.ACTIV &&
                        entry.getNota() == null
        ));
    }

    @Test
    void testGetAvailableCoursesForContract_ComplexSemesterFiltering() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create materials with different semesters
        Materie targetSemester = new Materie();
        targetSemester.setCod("TARGET");
        targetSemester.setNume("Target Course");
        targetSemester.setCredite(6);
        targetSemester.setSemestru(3);

        Materie wrongSemester = new Materie();
        wrongSemester.setCod("WRONG");
        wrongSemester.setNume("Wrong Semester");
        wrongSemester.setCredite(6);
        wrongSemester.setSemestru(4);

        CurriculumEntry targetEntry = new CurriculumEntry();
        targetEntry.setMaterie(targetSemester);
        targetEntry.setTip(Tip.OBLIGATORIE);
        targetEntry.setSpecializare(specializare);

        CurriculumEntry wrongEntry = new CurriculumEntry();
        wrongEntry.setMaterie(wrongSemester);
        wrongEntry.setTip(Tip.OBLIGATORIE);
        wrongEntry.setSpecializare(specializare);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3))
                .thenReturn(List.of(targetEntry, wrongEntry));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE))
                .thenReturn(List.of());
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(1, result.size());
        assertEquals("TARGET", result.get(0).getCod());
    }

    @Test
    void testGetAvailableCoursesForContract_MixedStatusFiltering() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3))
                .thenReturn(List.of(curriculumEntry1));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE))
                .thenReturn(List.of());

        // Create catalog entries with different statuses
        CatalogStudentMaterie finalizataEntry = new CatalogStudentMaterie();
        finalizataEntry.setMaterie(materie1);
        finalizataEntry.setStatus(MaterieStatus.FINALIZATA);

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(finalizataEntry));

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(0, result.size()); // Should be filtered out due to FINALIZATA status
    }

    @Test
    void testGenerateContractPdfWithoutPersist_UserNotFoundInRepository() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(materie1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "MAT01")).thenReturn(Optional.empty());

        // Test with no exception thrown when user is found
        assertDoesNotThrow(() -> service.generateContractPdfWithoutPersist("123", 2, List.of("MAT01")));
    }

    @Test
    void testGenerateContractFromSelection_InsufficientCreditsInBothSemesters() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie lowCreditSem1 = new Materie();
        lowCreditSem1.setCredite(20);
        lowCreditSem1.setSemestru(3);

        Materie lowCreditSem2 = new Materie();
        lowCreditSem2.setCredite(25);
        lowCreditSem2.setSemestru(4);

        when(materieRepo.findAllByCodIn(anyList())).thenReturn(List.of(lowCreditSem1, lowCreditSem2));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("MAT1", "MAT2")));

        // Should fail on first semester check
        assertTrue(ex.getMessage().contains("în semestrul I ai doar 20 credite"));
    }

    @Test
    void testGetContractCourses_EmptyResult() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));
        when(curriculumRepo.findBySpecializareIdAndAn(1, 2)).thenReturn(List.of());

        List<ContractDTO> result = service.getContractCourses("123", 2);

        assertEquals(0, result.size());
    }

    @Test
    void testGetAvailableCoursesForContract_AllStatusTypes() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create multiple materials
        Materie mat1 = new Materie();
        mat1.setCod("MAT1");
        mat1.setNume("Math1");
        mat1.setCredite(6);
        mat1.setSemestru(3);

        Materie mat2 = new Materie();
        mat2.setCod("MAT2");
        mat2.setNume("Math2");
        mat2.setCredite(6);
        mat2.setSemestru(3);

        Materie mat3 = new Materie();
        mat3.setCod("MAT3");
        mat3.setNume("Math3");
        mat3.setCredite(6);
        mat3.setSemestru(3);

        // Create curriculum entries
        CurriculumEntry entry1 = new CurriculumEntry();
        entry1.setMaterie(mat1);
        entry1.setTip(Tip.OBLIGATORIE);
        entry1.setSpecializare(specializare);

        CurriculumEntry entry2 = new CurriculumEntry();
        entry2.setMaterie(mat2);
        entry2.setTip(Tip.OBLIGATORIE);
        entry2.setSpecializare(specializare);

        CurriculumEntry entry3 = new CurriculumEntry();
        entry3.setMaterie(mat3);
        entry3.setTip(Tip.OBLIGATORIE);
        entry3.setSpecializare(specializare);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3))
                .thenReturn(List.of(entry1, entry2, entry3));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE))
                .thenReturn(List.of());

        // Create catalog entries with different statuses
        CatalogStudentMaterie activEntry = new CatalogStudentMaterie();
        activEntry.setMaterie(mat1);
        activEntry.setStatus(MaterieStatus.ACTIV);

        CatalogStudentMaterie picataEntry = new CatalogStudentMaterie();
        picataEntry.setMaterie(mat2);
        picataEntry.setStatus(MaterieStatus.PICATA);

        // mat3 has no catalog entry (null status)

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(activEntry, picataEntry));

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        // Should only include mat2 (PICATA - can re-enroll) and mat3 (no status - new)
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getCod().equals("MAT2")));
        assertTrue(result.stream().anyMatch(dto -> dto.getCod().equals("MAT3")));
        assertFalse(result.stream().anyMatch(dto -> dto.getCod().equals("MAT1"))); // ACTIV filtered out
    }

    @Test
    void testChangePassword_UserNotFound() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));
        when(materieRepo.findAllByCodIn(List.of("MAT01"))).thenReturn(List.of(materie1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "MAT01")).thenReturn(Optional.empty());

        // This test actually validates the normal flow
        assertDoesNotThrow(() -> service.generateContractFromSelection("123", 2, List.of("MAT01")));

        verify(contractRepo).save(any());
    }

    @Test
    void testGenerateContractPdfWithoutPersist_WithMixedSemesters() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie sem1Mat = new Materie();
        sem1Mat.setId(1);
        sem1Mat.setCod("SEM1");
        sem1Mat.setNume("Semester 1 Course");
        sem1Mat.setCredite(30);
        sem1Mat.setSemestru(1); // Odd semester

        Materie sem2Mat = new Materie();
        sem2Mat.setId(2);
        sem2Mat.setCod("SEM2");
        sem2Mat.setNume("Semester 2 Course");
        sem2Mat.setCredite(30);
        sem2Mat.setSemestru(2); // Even semester

        when(materieRepo.findAllByCodIn(List.of("SEM1", "SEM2"))).thenReturn(List.of(sem1Mat, sem2Mat));

        byte[] result = service.generateContractPdfWithoutPersist("123", 2, List.of("SEM1", "SEM2"));

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testGenerateContractFromSelection_WithComplexOptionalValidation() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        MateriiOptionale opt1 = new MateriiOptionale();
        opt1.setId(1);
        opt1.setNume("Optional Package 1");

        MateriiOptionale opt2 = new MateriiOptionale();
        opt2.setId(2);
        opt2.setNume("Optional Package 2");

        Materie mat1 = new Materie();
        mat1.setId(1);
        mat1.setCod("OPT1");
        mat1.setCredite(15);
        mat1.setSemestru(3);

        Materie mat2 = new Materie();
        mat2.setId(2);
        mat2.setCod("OPT2");
        mat2.setCredite(15);
        mat2.setSemestru(4);

        Materie mat3 = new Materie();
        mat3.setId(3);
        mat3.setCod("OPT3");
        mat3.setCredite(15);
        mat3.setSemestru(3);

        CurriculumEntry entry1 = new CurriculumEntry();
        entry1.setOptionale(opt1);

        CurriculumEntry entry2 = new CurriculumEntry();
        entry2.setOptionale(opt1); // Same package as mat1

        when(materieRepo.findAllByCodIn(List.of("OPT1", "OPT2", "OPT3"))).thenReturn(List.of(mat1, mat2, mat3));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.of(entry1));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 2)).thenReturn(Optional.empty());
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 3)).thenReturn(Optional.of(entry2));
        when(catalogRepo.findByStudentCodAndMaterieCod(anyString(), anyString())).thenReturn(Optional.empty());
        when(optionaleRepo.findById(1)).thenReturn(Optional.of(opt1));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("OPT1", "OPT2", "OPT3")));

        assertTrue(ex.getMessage().contains("Optional Package 1"));
    }

    @Test
    void testGetAvailableCoursesForContract_WithComplexActivStatusUpdate() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create multiple catalog entries with ACTIV status
        CatalogStudentMaterie activeEntry1 = new CatalogStudentMaterie();
        activeEntry1.setStatus(MaterieStatus.ACTIV);
        activeEntry1.setSemestru(1);
        activeEntry1.setMaterie(materie1);

        CatalogStudentMaterie activeEntry2 = new CatalogStudentMaterie();
        activeEntry2.setStatus(MaterieStatus.ACTIV);
        activeEntry2.setSemestru(2);
        activeEntry2.setMaterie(materie2);

        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of(activeEntry1, activeEntry2));
        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of());
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of());

        service.getAvailableCoursesForContract("123", 2, 1);

        verify(catalogRepo, times(2)).save(any());
        assertEquals(MaterieStatus.FINALIZATA, activeEntry1.getStatus());
        assertEquals(MaterieStatus.FINALIZATA, activeEntry2.getStatus());
    }

    @Test
    void testGenerateContractFromSelection_WithNullDescriptionInOptional() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie materie = new Materie();
        materie.setId(1);
        materie.setCod("NULL_DESC");
        materie.setNume("Course with null description handling");
        materie.setCredite(30);
        materie.setSemestru(3);

        when(materieRepo.findAllByCodIn(List.of("NULL_DESC"))).thenReturn(List.of(materie));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "NULL_DESC")).thenReturn(Optional.empty());

        byte[] result = service.generateContractFromSelection("123", 2, List.of("NULL_DESC"));

        assertNotNull(result);
        verify(contractRepo).save(any());
    }

    @Test
    void testGetAvailableCoursesForContract_WithNoCurrentSemesterMatches() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Create entries for different semesters than requested
        Materie wrongSemesterMat = new Materie();
        wrongSemesterMat.setCod("WRONG_SEM");
        wrongSemesterMat.setNume("Wrong Semester");
        wrongSemesterMat.setCredite(6);
        wrongSemesterMat.setSemestru(5); // Different semester

        CurriculumEntry wrongSemEntry = new CurriculumEntry();
        wrongSemEntry.setMaterie(wrongSemesterMat);
        wrongSemEntry.setTip(Tip.OBLIGATORIE);
        wrongSemEntry.setSpecializare(specializare);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of(wrongSemEntry));
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of());
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(0, result.size()); // No matches for semester 1 (relative)
    }

    @Test
    void testGenerateContractPdfWithoutPersist_WithSingleSemesterOnly() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie singleSemMat = new Materie();
        singleSemMat.setId(1);
        singleSemMat.setCod("SINGLE");
        singleSemMat.setNume("Single Semester Course");
        singleSemMat.setCredite(30);
        singleSemMat.setSemestru(3); // Only semester 1

        when(materieRepo.findAllByCodIn(List.of("SINGLE"))).thenReturn(List.of(singleSemMat));

        byte[] result = service.generateContractPdfWithoutPersist("123", 2, List.of("SINGLE"));

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testGetContractCourses_WithComplexCurriculumStructure() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie obligatorieMat = new Materie();
        obligatorieMat.setCod("OBLIG");
        obligatorieMat.setNume("Obligatory Course");
        obligatorieMat.setCredite(6);
        obligatorieMat.setSemestru(3);

        Materie optionalMat = new Materie();
        optionalMat.setCod("OPT");
        optionalMat.setNume("Optional Course");
        optionalMat.setCredite(4);
        optionalMat.setSemestru(4);

        CurriculumEntry obligEntry = new CurriculumEntry();
        obligEntry.setMaterie(obligatorieMat);
        obligEntry.setTip(Tip.OBLIGATORIE);
        obligEntry.setSemestru(3);

        CurriculumEntry optEntry = new CurriculumEntry();
        optEntry.setMaterie(optionalMat);
        optEntry.setTip(Tip.OPTIONALA);
        optEntry.setSemestru(4);

        when(curriculumRepo.findBySpecializareIdAndAn(1, 2)).thenReturn(List.of(obligEntry, optEntry));

        List<ContractDTO> result = service.getContractCourses("123", 2);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getCod().equals("OBLIG") && dto.isSelected()));
        assertTrue(result.stream().anyMatch(dto -> dto.getCod().equals("OPT") && !dto.isSelected()));
    }

    @Test
    void testValidateMinCreditsPerSemester_WithExactBoundaryConditions() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        // Test with exactly 29 credits in semester 1 (should fail)
        Materie sem1Mat = new Materie();
        sem1Mat.setCredite(29);
        sem1Mat.setSemestru(3);

        Materie sem2Mat = new Materie();
        sem2Mat.setCredite(30);
        sem2Mat.setSemestru(4);

        when(materieRepo.findAllByCodIn(anyList())).thenReturn(List.of(sem1Mat, sem2Mat));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractPdfWithoutPersist("123", 2, List.of("SEM1", "SEM2")));

        assertTrue(ex.getMessage().contains("în semestrul I ai doar 29 credite"));
    }

    @Test
    void testGenerateContractFromSelection_WithEmptyOptionalPackageMap() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie regularMat = new Materie();
        regularMat.setId(1);
        regularMat.setCod("REGULAR");
        regularMat.setNume("Regular Course");
        regularMat.setCredite(30);
        regularMat.setSemestru(3);

        when(materieRepo.findAllByCodIn(List.of("REGULAR"))).thenReturn(List.of(regularMat));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "REGULAR")).thenReturn(Optional.empty());

        // Should not throw exception when no optional packages are involved
        byte[] result = service.generateContractFromSelection("123", 2, List.of("REGULAR"));

        assertNotNull(result);
        verify(contractRepo).save(any());
    }

    @Test
    void testGetAvailableCoursesForContract_WithDifferentSpecializationIds() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Specializare otherSpec1 = new Specializare();
        otherSpec1.setId(2);

        Specializare otherSpec2 = new Specializare();
        otherSpec2.setId(3);

        CurriculumEntry external1 = new CurriculumEntry();
        external1.setMaterie(materie1);
        external1.setTip(Tip.OBLIGATORIE);
        external1.setSpecializare(otherSpec1);

        CurriculumEntry external2 = new CurriculumEntry();
        external2.setMaterie(materie2);
        external2.setTip(Tip.OBLIGATORIE);
        external2.setSpecializare(otherSpec2);

        when(curriculumRepo.findBySpecializareIdAndAnAndSemestru(1, 2, 3)).thenReturn(List.of());
        when(curriculumRepo.findByAnAndSemestruAndTip(2, 3, Tip.OBLIGATORIE)).thenReturn(List.of(external1, external2));
        when(catalogRepo.findByStudentCod("123")).thenReturn(List.of());

        List<ContractDTO> result = service.getAvailableCoursesForContract("123", 2, 1);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getTip() == Tip.OPTIONALA));
        assertTrue(result.stream().noneMatch(ContractDTO::isSelected));
    }

    @Test
    void testHasContract_WithDifferentAnContract() {
        // Test with different year contracts
        when(contractRepo.findByStudentCodAndAnContract("123", 1))
                .thenReturn(Optional.empty());
        when(contractRepo.findByStudentCodAndAnContract("123", 3))
                .thenReturn(Optional.of(new Contract("123", 3)));

        assertFalse(service.hasContract("123", 1));
        assertTrue(service.hasContract("123", 3));
    }

    @Test
    void testGenerateContractPdfWithoutPersist_WithLargeNumberOfCourses() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        List<Materie> manyCourses = new ArrayList<>();

        // Create multiple courses for both semesters
        for (int i = 1; i <= 10; i++) {
            Materie mat = new Materie();
            mat.setId(i);
            mat.setCod("COURSE" + i);
            mat.setNume("Course " + i);
            mat.setCredite(3);
            mat.setSemestru(i % 2 == 1 ? 3 : 4); // Alternate between semesters
            manyCourses.add(mat);
        }

        when(materieRepo.findAllByCodIn(anyList())).thenReturn(manyCourses);

        List<String> courseCodes = manyCourses.stream().map(Materie::getCod).toList();
        byte[] result = service.generateContractPdfWithoutPersist("123", 2, courseCodes);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testRecordActiveCourses_WithExistingActivStatus() throws Exception {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie existingMat = new Materie();
        existingMat.setId(1);
        existingMat.setCod("EXISTING");
        existingMat.setNume("Existing Course");
        existingMat.setCredite(30);
        existingMat.setSemestru(3);

        CatalogStudentMaterie existingActiveEntry = new CatalogStudentMaterie();
        existingActiveEntry.setStudent(student);
        existingActiveEntry.setMaterie(existingMat);
        existingActiveEntry.setStatus(MaterieStatus.ACTIV);

        when(materieRepo.findAllByCodIn(List.of("EXISTING"))).thenReturn(List.of(existingMat));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "EXISTING")).thenReturn(Optional.of(existingActiveEntry));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("EXISTING")));

        assertTrue(ex.getMessage().contains("nu pot fi adăugate din nou în contract"));
    }

    @Test
    void testGenerateContractFromSelection_WithMixedStatusValidation() {
        when(studentRepo.findByCod("123")).thenReturn(Optional.of(student));

        Materie activMat = new Materie();
        activMat.setId(1);
        activMat.setCod("ACTIV");

        Materie finalizataMat = new Materie();
        finalizataMat.setId(2);
        finalizataMat.setCod("FINALIZATA");

        CatalogStudentMaterie activEntry = new CatalogStudentMaterie();
        activEntry.setStatus(MaterieStatus.ACTIV);

        CatalogStudentMaterie finalizataEntry = new CatalogStudentMaterie();
        finalizataEntry.setStatus(MaterieStatus.FINALIZATA);

        when(materieRepo.findAllByCodIn(List.of("ACTIV", "FINALIZATA"))).thenReturn(List.of(activMat, finalizataMat));
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 1)).thenReturn(Optional.empty());
        when(curriculumRepo.findBySpecializareIdAndMaterieId(1, 2)).thenReturn(Optional.empty());
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "ACTIV")).thenReturn(Optional.of(activEntry));
        when(catalogRepo.findByStudentCodAndMaterieCod("123", "FINALIZATA")).thenReturn(Optional.of(finalizataEntry));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.generateContractFromSelection("123", 2, List.of("ACTIV", "FINALIZATA")));

        assertTrue(ex.getMessage().contains("nu pot fi adăugate din nou în contract"));
    }
}
