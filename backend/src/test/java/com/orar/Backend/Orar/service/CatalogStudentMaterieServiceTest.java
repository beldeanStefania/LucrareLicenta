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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogStudentMaterieServiceTest {

    @Mock
    private CatalogStudentMaterieRepository catalogStudentMaterieRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MaterieRepository materieRepository;

    @InjectMocks
    private CatalogStudentMaterieService service;

    private Student sampleStudent;
    private Materie sampleMaterie;
    private CatalogStudentMaterie sampleCatalog;
    private CatalogStudentMaterieDTO sampleDTO;

    @BeforeEach
    void setup() {
        sampleStudent = new Student();
        sampleStudent.setId(1);
        sampleStudent.setCod("STU001");
        sampleStudent.setNume("Popescu");
        sampleStudent.setPrenume("Ion");

        sampleMaterie = new Materie();
        sampleMaterie.setId(1);
        sampleMaterie.setCod("MAT001");
        sampleMaterie.setNume("Matematica");
        sampleMaterie.setCredite(6);
        sampleMaterie.setSemestru(1);

        sampleCatalog = new CatalogStudentMaterie();
        sampleCatalog.setId(1);
        sampleCatalog.setStudent(sampleStudent);
        sampleCatalog.setMaterie(sampleMaterie);
        sampleCatalog.setNota(8.5);
        sampleCatalog.setSemestru(1);
        sampleCatalog.setStatus(MaterieStatus.FINALIZATA);

        sampleDTO = new CatalogStudentMaterieDTO();
        sampleDTO.setStudentCod("STU001");
        sampleDTO.setNumeMaterie("Matematica");
        sampleDTO.setNota(8.5);
        sampleDTO.setSemestru(1);
    }

    @Test
    @DisplayName("getAll should return mapped DTOs")
    void testGetAll() {
        when(catalogStudentMaterieRepository.findAll()).thenReturn(List.of(sampleCatalog));

        List<CatalogStudentMaterieDTO> result = service.getAll();

        assertEquals(1, result.size());
        CatalogStudentMaterieDTO dto = result.get(0);
        assertEquals(8.5, dto.getNota());
        assertEquals(1, dto.getSemestru());
        assertEquals("STU001", dto.getStudentCod());
        assertEquals("MAT001", dto.getCodMaterie());
        verify(catalogStudentMaterieRepository).findAll();
    }

    @Test
    @DisplayName("getAll should return empty list when no entries")
    void testGetAllEmpty() {
        when(catalogStudentMaterieRepository.findAll()).thenReturn(List.of());

        List<CatalogStudentMaterieDTO> result = service.getAll();

        assertTrue(result.isEmpty());
        verify(catalogStudentMaterieRepository).findAll();
    }

    @Test
    @DisplayName("getNoteByStudent should return student's grades")
    void testGetNoteByStudent() {
        when(catalogStudentMaterieRepository.findByStudentCod("STU001")).thenReturn(List.of(sampleCatalog));

        List<CatalogStudentMaterieDTO> result = service.getNoteByStudent("STU001");

        assertEquals(1, result.size());
        CatalogStudentMaterieDTO dto = result.get(0);
        assertEquals(8.5, dto.getNota());
        assertEquals(1, dto.getSemestru());
        assertEquals("STU001", dto.getStudentCod());
        assertEquals("MAT001", dto.getCodMaterie());
        assertEquals("Matematica", dto.getNumeMaterie());
        assertEquals(6, dto.getCredite());
        verify(catalogStudentMaterieRepository).findByStudentCod("STU001");
    }

    @Test
    @DisplayName("getNoteByStudent should return empty list when student has no grades")
    void testGetNoteByStudentEmpty() {
        when(catalogStudentMaterieRepository.findByStudentCod("STU002")).thenReturn(List.of());

        List<CatalogStudentMaterieDTO> result = service.getNoteByStudent("STU002");

        assertTrue(result.isEmpty());
        verify(catalogStudentMaterieRepository).findByStudentCod("STU002");
    }

    @Nested
    @DisplayName("addOrUpdate method tests")
    class AddOrUpdateTests {

        @Test
        @DisplayName("Should add new catalog entry when not exists")
        void testAddOrUpdateNewEntry() throws Exception {
            when(studentRepository.findByCod("STU001")).thenReturn(Optional.of(sampleStudent));
            when(materieRepository.findByNume("Matematica")).thenReturn(Optional.of(sampleMaterie));
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.empty());
            when(catalogStudentMaterieRepository.save(any(CatalogStudentMaterie.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CatalogStudentMaterie result = service.addOrUpdate(sampleDTO);

            assertNotNull(result);
            assertEquals(8.5, result.getNota());
            assertEquals(1, result.getSemestru());
            assertEquals(MaterieStatus.ACTIV, result.getStatus());
            assertEquals(sampleStudent, result.getStudent());
            assertEquals(sampleMaterie, result.getMaterie());

            ArgumentCaptor<CatalogStudentMaterie> captor = ArgumentCaptor.forClass(CatalogStudentMaterie.class);
            verify(catalogStudentMaterieRepository).save(captor.capture());
            CatalogStudentMaterie saved = captor.getValue();
            assertEquals(8.5, saved.getNota());
            assertEquals(MaterieStatus.ACTIV, saved.getStatus());
        }

        @Test
        @DisplayName("Should update existing catalog entry when exists")
        void testAddOrUpdateExistingEntry() throws Exception {
            CatalogStudentMaterie existing = new CatalogStudentMaterie();
            existing.setId(1);
            existing.setStudent(sampleStudent);
            existing.setMaterie(sampleMaterie);
            existing.setNota(6.0);
            existing.setStatus(MaterieStatus.ACTIV);

            when(studentRepository.findByCod("STU001")).thenReturn(Optional.of(sampleStudent));
            when(materieRepository.findByNume("Matematica")).thenReturn(Optional.of(sampleMaterie));
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.of(existing));
            when(catalogStudentMaterieRepository.save(any(CatalogStudentMaterie.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CatalogStudentMaterie result = service.addOrUpdate(sampleDTO);

            assertNotNull(result);
            assertEquals(8.5, result.getNota());
            assertEquals(MaterieStatus.FINALIZATA, result.getStatus()); // nota >= 5
            verify(catalogStudentMaterieRepository).save(existing);
        }

        @Test
        @DisplayName("Should set status to PICATA when nota < 5")
        void testAddOrUpdateFailingGrade() throws Exception {
            sampleDTO.setNota(3.5);

            CatalogStudentMaterie existing = new CatalogStudentMaterie();
            existing.setStudent(sampleStudent);
            existing.setMaterie(sampleMaterie);
            existing.setNota(6.0);

            when(studentRepository.findByCod("STU001")).thenReturn(Optional.of(sampleStudent));
            when(materieRepository.findByNume("Matematica")).thenReturn(Optional.of(sampleMaterie));
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.of(existing));
            when(catalogStudentMaterieRepository.save(any(CatalogStudentMaterie.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CatalogStudentMaterie result = service.addOrUpdate(sampleDTO);

            assertEquals(3.5, result.getNota());
            assertEquals(MaterieStatus.PICATA, result.getStatus());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when studentCod is null")
        void testAddOrUpdateNullStudentCod() {
            sampleDTO.setStudentCod(null);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Codul studentului este obligatoriu!", exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when studentCod is empty")
        void testAddOrUpdateEmptyStudentCod() {
            sampleDTO.setStudentCod("");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Codul studentului este obligatoriu!", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when numeMaterie is null")
        void testAddOrUpdateNullNumeMaterie() {
            sampleDTO.setNumeMaterie(null);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Numele materiei este obligatoriu!", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when numeMaterie is empty")
        void testAddOrUpdateEmptyNumeMaterie() {
            sampleDTO.setNumeMaterie("");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Numele materiei este obligatoriu!", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when nota is null")
        void testAddOrUpdateNullNota() {
            sampleDTO.setNota(null);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Nota este obligatorie!", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw StudentNotFoundException when student not found")
        void testAddOrUpdateStudentNotFound() {
            when(studentRepository.findByCod("STU001")).thenReturn(Optional.empty());

            StudentNotFoundException exception = assertThrows(
                    StudentNotFoundException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Student not found with Cod: STU001", exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw MaterieNotFoundException when materie not found")
        void testAddOrUpdateMaterieNotFound() {
            when(studentRepository.findByCod("STU001")).thenReturn(Optional.of(sampleStudent));
            when(materieRepository.findByNume("Matematica")).thenReturn(Optional.empty());

            MaterieNotFoundException exception = assertThrows(
                    MaterieNotFoundException.class,
                    () -> service.addOrUpdate(sampleDTO)
            );

            assertEquals("Materie not found with Name: Matematica", exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update method tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update existing catalog entry successfully")
        void testUpdateSuccess() throws Exception {
            CatalogStudentMaterie existing = new CatalogStudentMaterie();
            existing.setId(1);
            existing.setStudent(sampleStudent);
            existing.setMaterie(sampleMaterie);
            existing.setNota(6.0);
            existing.setSemestru(1);
            existing.setStatus(MaterieStatus.ACTIV);

            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.of(existing));
            when(catalogStudentMaterieRepository.save(any(CatalogStudentMaterie.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CatalogStudentMaterie result = service.update("STU001", "MAT001", sampleDTO);

            assertNotNull(result);
            assertEquals(8.5, result.getNota());
            assertEquals(1, result.getSemestru());
            assertEquals(MaterieStatus.FINALIZATA, result.getStatus());
            verify(catalogStudentMaterieRepository).save(existing);
        }

        @Test
        @DisplayName("Should set status to PICATA when updating with failing grade")
        void testUpdateWithFailingGrade() throws Exception {
            sampleDTO.setNota(4.0);

            CatalogStudentMaterie existing = new CatalogStudentMaterie();
            existing.setStudent(sampleStudent);
            existing.setMaterie(sampleMaterie);
            existing.setNota(8.0);
            existing.setStatus(MaterieStatus.FINALIZATA);

            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.of(existing));
            when(catalogStudentMaterieRepository.save(any(CatalogStudentMaterie.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CatalogStudentMaterie result = service.update("STU001", "MAT001", sampleDTO);

            assertEquals(4.0, result.getNota());
            assertEquals(MaterieStatus.PICATA, result.getStatus());
        }

        @Test
        @DisplayName("Should throw CatalogStudentMaterieNotFoundException when entry not found")
        void testUpdateNotFound() {
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.empty());

            CatalogStudentMaterieNotFoundException exception = assertThrows(
                    CatalogStudentMaterieNotFoundException.class,
                    () -> service.update("STU001", "MAT001", sampleDTO)
            );

            assertEquals("Catalog entry not found for Student Cod: STU001 and Materie Cod: MAT001",
                    exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete method tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete catalog entry by id successfully")
        void testDeleteById() throws Exception {
            when(catalogStudentMaterieRepository.findById(1)).thenReturn(Optional.of(sampleCatalog));

            service.delete(1);

            verify(catalogStudentMaterieRepository).findById(1);
            verify(catalogStudentMaterieRepository).delete(sampleCatalog);
        }

        @Test
        @DisplayName("Should throw CatalogStudentMaterieNotFoundException when entry not found by id")
        void testDeleteByIdNotFound() {
            when(catalogStudentMaterieRepository.findById(999)).thenReturn(Optional.empty());

            CatalogStudentMaterieNotFoundException exception = assertThrows(
                    CatalogStudentMaterieNotFoundException.class,
                    () -> service.delete(999)
            );

            assertEquals("Catalog entry not found with ID: 999", exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should delete catalog entry by student and materie codes successfully")
        void testDeleteByStudentAndMaterie() throws Exception {
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.of(sampleCatalog));

            service.deleteByStudentAndMaterie("STU001", "MAT001");

            verify(catalogStudentMaterieRepository).findByStudentCodAndMaterieCod("STU001", "MAT001");
            verify(catalogStudentMaterieRepository).delete(sampleCatalog);
        }

        @Test
        @DisplayName("Should throw CatalogStudentMaterieNotFoundException when entry not found by codes")
        void testDeleteByStudentAndMaterieNotFound() {
            when(catalogStudentMaterieRepository.findByStudentCodAndMaterieCod("STU001", "MAT001"))
                    .thenReturn(Optional.empty());

            CatalogStudentMaterieNotFoundException exception = assertThrows(
                    CatalogStudentMaterieNotFoundException.class,
                    () -> service.deleteByStudentAndMaterie("STU001", "MAT001")
            );

            assertEquals("Catalog entry not found for Student Cod: STU001 and Materie Cod: MAT001",
                    exception.getMessage());
            verify(catalogStudentMaterieRepository, never()).delete(any());
        }
    }

    @Test
    @DisplayName("Should handle multiple catalog entries for same student")
    void testMultipleEntriesForStudent() {
        Materie materie2 = new Materie();
        materie2.setId(2);
        materie2.setCod("PHY001");
        materie2.setNume("Fizica");
        materie2.setCredite(5);
        materie2.setSemestru(2);

        CatalogStudentMaterie catalog2 = new CatalogStudentMaterie();
        catalog2.setId(2);
        catalog2.setStudent(sampleStudent);
        catalog2.setMaterie(materie2);
        catalog2.setNota(7.0);
        catalog2.setSemestru(2);
        catalog2.setStatus(MaterieStatus.FINALIZATA);

        when(catalogStudentMaterieRepository.findByStudentCod("STU001"))
                .thenReturn(List.of(sampleCatalog, catalog2));

        List<CatalogStudentMaterieDTO> result = service.getNoteByStudent("STU001");

        assertEquals(2, result.size());

        // Check first entry
        CatalogStudentMaterieDTO dto1 = result.get(0);
        assertEquals("Matematica", dto1.getNumeMaterie());
        assertEquals(8.5, dto1.getNota());

        // Check second entry
        CatalogStudentMaterieDTO dto2 = result.get(1);
        assertEquals("Fizica", dto2.getNumeMaterie());
        assertEquals(7.0, dto2.getNota());
    }
}
