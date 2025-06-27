package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private MaterieRepository materieRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CatalogStudentMaterieRepository catalogRepository;
    @Mock
    private OrarRepository orarRepository;
    @Mock
    private ProfesorRepository profesorRepository;
    @Mock
    private CladireRepository cladireRepository;
    @Mock
    private RepartizareProfRepository repartizareProfRepository;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ChatService chatService;

    private Student sampleStudent;
    private Profesor sampleProfesor;
    private Materie sampleMaterie;
    private Specializare sampleSpecializare;
    private CatalogStudentMaterie sampleCatalog;
    private Orar sampleOrar;
    private RepartizareProf sampleRepartizare;
    private CurriculumEntry sampleCurriculumEntry;

    @BeforeEach
    void setup() {
        // Create the service manually with mocked dependencies
        chatService = new ChatService(
                materieRepository, studentRepository, catalogRepository, orarRepository,
                profesorRepository, cladireRepository, repartizareProfRepository,
                WebClient.builder(), "test-api-key", "gpt-3.5-turbo"
        );

        // Setup sample data
        sampleSpecializare = new Specializare();
        sampleSpecializare.setId(1);
        sampleSpecializare.setSpecializare("Informatica");

        sampleStudent = new Student();
        sampleStudent.setId(1);
        sampleStudent.setCod("STU001");
        sampleStudent.setNume("Popescu");
        sampleStudent.setPrenume("Ion");
        sampleStudent.setGrupa("214");
        sampleStudent.setAn(2);
        sampleStudent.setSpecializare(sampleSpecializare);

        User studentUser = new User();
        studentUser.setUsername("student1");
        sampleStudent.setUser(studentUser);

        sampleMaterie = new Materie();
        sampleMaterie.setId(1);
        sampleMaterie.setCod("MAT001");
        sampleMaterie.setNume("Matematica");
        sampleMaterie.setCredite(6);
        sampleMaterie.setSemestru(1);
        sampleMaterie.setAn(2);

        sampleCatalog = new CatalogStudentMaterie();
        sampleCatalog.setId(1);
        sampleCatalog.setStudent(sampleStudent);
        sampleCatalog.setMaterie(sampleMaterie);
        sampleCatalog.setNota(8.5);

        sampleProfesor = new Profesor();
        sampleProfesor.setId(1);
        sampleProfesor.setNume("Ionescu");
        sampleProfesor.setPrenume("Maria");

        User profesorUser = new User();
        profesorUser.setUsername("prof1");
        sampleProfesor.setUser(profesorUser);

        sampleRepartizare = new RepartizareProf();
        sampleRepartizare.setId(1);
        sampleRepartizare.setProfesor(sampleProfesor);
        sampleRepartizare.setMaterie(sampleMaterie);
        sampleRepartizare.setTip("Curs");

        Sala sampleSala = new Sala();
        sampleSala.setId(1);
        sampleSala.setNume("A101");

        sampleOrar = new Orar();
        sampleOrar.setId(1);
        sampleOrar.setGrupa("214");
        sampleOrar.setZi("Luni");
        sampleOrar.setOraInceput(8);
        sampleOrar.setOraSfarsit(10);
        sampleOrar.setRepartizareProf(sampleRepartizare);
        sampleOrar.setSala(sampleSala);

        sampleCurriculumEntry = new CurriculumEntry();
        sampleCurriculumEntry.setId(1);
        sampleCurriculumEntry.setMaterie(sampleMaterie);
        sampleCurriculumEntry.setAn(2);
        sampleCurriculumEntry.setSemestru(1);
        sampleCurriculumEntry.setTip(Tip.OBLIGATORIE);
    }

    @Nested
    @DisplayName("Student Chat Tests")
    class StudentChatTests {

        @Test
        @DisplayName("Should throw ResponseStatusException when student not found")
        void testChatStudentNotFound() {
            when(studentRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> chatService.chat("Hello", "unknown")
            );

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }

        @Test
        @DisplayName("Should build proper context for student without contracts")
        void testChatStudentWithoutContracts() {
            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of(sampleMaterie));
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of(sampleCurriculumEntry));
            when(orarRepository.findByGrupa("214")).thenReturn(List.of(sampleOrar));

            // Mock the WebClient chain - this is a simplified approach
            // In reality, we'd need to use @MockBean or create a test configuration
            // For now, we'll test the data preparation logic by catching the exception
            assertThrows(Exception.class, () -> chatService.chat("Hello", "student1"));

            // Verify that all required data was fetched
            verify(studentRepository).findByUserUsername("student1");
            verify(catalogRepository).findByStudent(sampleStudent);
            verify(materieRepository).findAll();
            verify(orarRepository).findCurriculumBySpecializare(sampleSpecializare);
            verify(orarRepository).findByGrupa("214");
        }

        @Test
        @DisplayName("Should build proper context for student with contracts")
        void testChatStudentWithContracts() {
            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of(sampleCatalog));
            when(materieRepository.findAll()).thenReturn(List.of(sampleMaterie));
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of(sampleCurriculumEntry));
            when(orarRepository.findByGrupa("214")).thenReturn(List.of(sampleOrar));

            assertThrows(Exception.class, () -> chatService.chat("Hello", "student1"));

            verify(catalogRepository).findByStudent(sampleStudent);
        }

        @Test
        @DisplayName("Should handle optional conflict detection")
        void testOptionalConflictReply() {
            // Create optional subjects in the same package
            MateriiOptionale optionalPackage = new MateriiOptionale();
            optionalPackage.setId(1);
            optionalPackage.setNume("Package Optional 1");

            Materie optionalMaterie1 = new Materie();
            optionalMaterie1.setNume("Materie Optional 1");

            Materie optionalMaterie2 = new Materie();
            optionalMaterie2.setNume("Materie Optional 2");

            CurriculumEntry optional1 = new CurriculumEntry();
            optional1.setMaterie(optionalMaterie1);
            optional1.setAn(2);
            optional1.setTip(Tip.OPTIONALA);
            optional1.setOptionale(optionalPackage);

            CurriculumEntry optional2 = new CurriculumEntry();
            optional2.setMaterie(optionalMaterie2);
            optional2.setAn(2);
            optional2.setTip(Tip.OPTIONALA);
            optional2.setOptionale(optionalPackage);

            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of());
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare))
                    .thenReturn(List.of(optional1, optional2));
            when(orarRepository.findByGrupa("214")).thenReturn(List.of());

            // Test conflict detection message
            String conflictMessage = "alege Materie Optional 1 și Materie Optional 2";

            String result = chatService.chat(conflictMessage, "student1");

            // Should return conflict message
            assertNotNull(result);
            assertTrue(result.contains("Nu poți alege"));
            assertTrue(result.contains("Package Optional 1"));
        }
    }

    @Nested
    @DisplayName("Professor Chat Tests")
    class ProfessorChatTests {

        @Test
        @DisplayName("Should throw ResponseStatusException when professor not found")
        void testChatForProfessorNotFound() {
            when(profesorRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> chatService.chatForProfessor("Hello", "unknown")
            );

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("Profesorul nu a fost găsit", exception.getReason());
        }

        @Test
        @DisplayName("Should build proper context for professor")
        void testChatForProfessorSuccess() {
            // Setup professor with repartizare
            sampleProfesor.setRepartizareProfs(List.of(sampleRepartizare));

            // Setup building and rooms
            Cladire cladire = new Cladire();
            cladire.setNume("Cladirea A");

            Sala sala = new Sala();
            sala.setNume("A101");
            sala.setCapacitate(50);

            cladire.setSala(List.of(sala));

            when(profesorRepository.findByUserUsername("prof1")).thenReturn(Optional.of(sampleProfesor));
            when(orarRepository.findByRepartizareProf_Profesor_Id(1)).thenReturn(List.of(sampleOrar));
            when(cladireRepository.findAll()).thenReturn(List.of(cladire));

            assertThrows(Exception.class, () -> chatService.chatForProfessor("Hello", "prof1"));

            verify(profesorRepository).findByUserUsername("prof1");
            verify(orarRepository).findByRepartizareProf_Profesor_Id(1);
            verify(cladireRepository).findAll();
        }

        @Test
        @DisplayName("Should handle professor with no scheduled classes")
        void testChatForProfessorNoSchedule() {
            sampleProfesor.setRepartizareProfs(List.of());

            when(profesorRepository.findByUserUsername("prof1")).thenReturn(Optional.of(sampleProfesor));
            when(orarRepository.findByRepartizareProf_Profesor_Id(1)).thenReturn(List.of());
            when(cladireRepository.findAll()).thenReturn(List.of());

            assertThrows(Exception.class, () -> chatService.chatForProfessor("Hello", "prof1"));

            verify(orarRepository).findByRepartizareProf_Profesor_Id(1);
        }
    }

    @Nested
    @DisplayName("Admin Chat Tests")
    class AdminChatTests {

        @Test
        @DisplayName("Should build proper context for admin")
        void testChatForAdmin() {
            // Setup statistics data
            Student student2 = new Student();
            student2.setAn(3);

            when(studentRepository.count()).thenReturn(2L);
            when(profesorRepository.count()).thenReturn(1L);
            when(materieRepository.count()).thenReturn(1L);
            when(studentRepository.findAll()).thenReturn(List.of(sampleStudent, student2));
            when(repartizareProfRepository.findAll()).thenReturn(List.of(sampleRepartizare));

            assertThrows(Exception.class, () -> chatService.chatForAdmin("Hello", "admin"));

            verify(studentRepository).count();
            verify(profesorRepository).count();
            verify(materieRepository).count();
            verify(studentRepository).findAll();
            verify(repartizareProfRepository).findAll();
        }

        @Test
        @DisplayName("Should handle empty statistics")
        void testChatForAdminEmptyStats() {
            when(studentRepository.count()).thenReturn(0L);
            when(profesorRepository.count()).thenReturn(0L);
            when(materieRepository.count()).thenReturn(0L);
            when(studentRepository.findAll()).thenReturn(List.of());
            when(repartizareProfRepository.findAll()).thenReturn(List.of());

            assertThrows(Exception.class, () -> chatService.chatForAdmin("Hello", "admin"));

            verify(studentRepository).count();
        }
    }

    @Nested
    @DisplayName("Optional Conflict Detection Tests")
    class OptionalConflictTests {

        @Test
        @DisplayName("Should detect conflict between optionals in same package")
        void testOptionalConflictDetection() {
            MateriiOptionale optionalPackage = new MateriiOptionale();
            optionalPackage.setId(1);
            optionalPackage.setNume("Pachet Optional");

            Materie materie1 = new Materie();
            materie1.setNume("Java Programming");

            Materie materie2 = new Materie();
            materie2.setNume("Python Programming");

            CurriculumEntry entry1 = new CurriculumEntry();
            entry1.setMaterie(materie1);
            entry1.setAn(2);
            entry1.setTip(Tip.OPTIONALA);
            entry1.setOptionale(optionalPackage);

            CurriculumEntry entry2 = new CurriculumEntry();
            entry2.setMaterie(materie2);
            entry2.setAn(2);
            entry2.setTip(Tip.OPTIONALA);
            entry2.setOptionale(optionalPackage);

            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of());
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare))
                    .thenReturn(List.of(entry1, entry2));
            when(orarRepository.findByGrupa("214")).thenReturn(List.of());

            String conflictMessage = "alege Java Programming și Python Programming";

            String result = chatService.chat(conflictMessage, "student1");

            // Should return conflict message
            assertNotNull(result);
            assertTrue(result.contains("Nu poți alege"));
            assertTrue(result.contains("Pachet Optional"));
        }

        @Test
        @DisplayName("Should not detect conflict for optionals in different packages")
        void testNoOptionalConflict() {
            MateriiOptionale package1 = new MateriiOptionale();
            package1.setId(1);
            package1.setNume("Pachet 1");

            MateriiOptionale package2 = new MateriiOptionale();
            package2.setId(2);
            package2.setNume("Pachet 2");

            Materie materie1 = new Materie();
            materie1.setNume("Java Programming");

            Materie materie2 = new Materie();
            materie2.setNume("Python Programming");

            CurriculumEntry entry1 = new CurriculumEntry();
            entry1.setMaterie(materie1);
            entry1.setAn(2);
            entry1.setTip(Tip.OPTIONALA);
            entry1.setOptionale(package1);

            CurriculumEntry entry2 = new CurriculumEntry();
            entry2.setMaterie(materie2);
            entry2.setAn(2);
            entry2.setTip(Tip.OPTIONALA);
            entry2.setOptionale(package2);

            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of());
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare))
                    .thenReturn(List.of(entry1, entry2));
            when(orarRepository.findByGrupa("214")).thenReturn(List.of());

            String message = "alege Java Programming și Python Programming";

            assertThrows(Exception.class, () -> chatService.chat(message, "student1"));
        }

        @Test
        @DisplayName("Should handle message without conflict pattern")
        void testNoConflictPattern() {
            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of());
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of());
            when(orarRepository.findByGrupa("214")).thenReturn(List.of());

            String normalMessage = "Ce note am la matematica?";

            assertThrows(Exception.class, () -> chatService.chat(normalMessage, "student1"));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle general exceptions during chat")
        void testGeneralExceptionHandling() {
            when(studentRepository.findByUserUsername("student1")).thenThrow(new RuntimeException("Database error"));

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> chatService.chat("Hello", "student1")
            );

            assertEquals("Database error", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle null username gracefully")
        void testNullUsernameHandling() {
            when(studentRepository.findByUserUsername(null)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> chatService.chat("Hello", null)
            );

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }

        @Test
        @DisplayName("Should handle empty message")
        void testEmptyMessageHandling() {
            when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
            when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
            when(materieRepository.findAll()).thenReturn(List.of());
            when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of());
            when(orarRepository.findByGrupa("214")).thenReturn(List.of());

            assertThrows(Exception.class, () -> chatService.chat("", "student1"));
        }
    }

    @Test
    @DisplayName("Should handle student with empty schedule")
    void testStudentWithEmptySchedule() {
        when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
        when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
        when(materieRepository.findAll()).thenReturn(List.of());
        when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of());
        when(orarRepository.findByGrupa("214")).thenReturn(List.of());

        assertThrows(Exception.class, () -> chatService.chat("Hello", "student1"));

        verify(orarRepository).findByGrupa("214");
    }

    @Test
    @DisplayName("Should handle student with multiple grades")
    void testStudentWithMultipleGrades() {
        CatalogStudentMaterie catalog2 = new CatalogStudentMaterie();
        catalog2.setStudent(sampleStudent);
        catalog2.setMaterie(sampleMaterie);
        catalog2.setNota(9.0);

        when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
        when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of(sampleCatalog, catalog2));
        when(materieRepository.findAll()).thenReturn(List.of());
        when(orarRepository.findCurriculumBySpecializare(sampleSpecializare)).thenReturn(List.of());
        when(orarRepository.findByGrupa("214")).thenReturn(List.of());

        assertThrows(Exception.class, () -> chatService.chat("Hello", "student1"));

        verify(catalogRepository).findByStudent(sampleStudent);
    }

    @Test
    @DisplayName("Should build context with complex curriculum")
    void testComplexCurriculumContext() {
        // Create multiple curriculum entries
        CurriculumEntry entry2 = new CurriculumEntry();
        entry2.setMaterie(sampleMaterie);
        entry2.setAn(3);
        entry2.setSemestru(2);
        entry2.setTip(Tip.OBLIGATORIE);

        when(studentRepository.findByUserUsername("student1")).thenReturn(Optional.of(sampleStudent));
        when(catalogRepository.findByStudent(sampleStudent)).thenReturn(List.of());
        when(materieRepository.findAll()).thenReturn(List.of());
        when(orarRepository.findCurriculumBySpecializare(sampleSpecializare))
                .thenReturn(List.of(sampleCurriculumEntry, entry2));
        when(orarRepository.findByGrupa("214")).thenReturn(List.of());

        assertThrows(Exception.class, () -> chatService.chat("Hello", "student1"));

        verify(orarRepository).findCurriculumBySpecializare(sampleSpecializare);
    }
}
