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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogStudentMaterieServiceTest {

    @Mock
    private CatalogStudentMaterieRepository catalogRepo;
    @Mock
    private StudentRepository studentRepo;
    @Mock
    private MaterieRepository materieRepo;

    @InjectMocks
    private CatalogStudentMaterieService service;

    private Student student;
    private Materie materie;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setCod("S1");
        materie = new Materie();
        materie.setCod("M1");
        materie.setNume("Matematica");
        materie.setCredite(5);
        materie.setSemestru(1);
    }

    @Test
    void testGetAll() {
        CatalogStudentMaterie entry = new CatalogStudentMaterie();
        entry.setId(1);
        entry.setNota(7.5);
        entry.setSemestru(2);
        entry.setStudent(student);
        entry.setMaterie(materie);
        when(catalogRepo.findAll()).thenReturn(List.of(entry));

        List<?> dtos = service.getAll();
        assertEquals(1, dtos.size());
        // Further validate DTO fields
    }

    @Test
    void testGetNoteByStudent() {
        CatalogStudentMaterie entry = new CatalogStudentMaterie();
        entry.setStudent(student);
        entry.setMaterie(materie);
        entry.setNota(4.0);
        entry.setSemestru(1);
        when(catalogRepo.findByStudentCod("S1")).thenReturn(List.of(entry));

        var list = service.getNoteByStudent("S1");
        assertEquals(1, list.size());
        var dto = list.get(0);
        assertEquals("S1", dto.getStudentCod());
        assertEquals("M1", dto.getCodMaterie());
        assertEquals(4.0, dto.getNota());
        assertEquals(5, dto.getCredite());
        assertEquals("Matematica", dto.getNumeMaterie());
        assertEquals(1, dto.getSemestru());
    }

    @Test
    void testAddOrUpdate_NewEntry() throws Exception {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        dto.setStudentCod("S1");
        dto.setNumeMaterie("Matematica");
        dto.setNota(6.0);
        dto.setSemestru(2);

        when(studentRepo.findByCod("S1")).thenReturn(Optional.of(student));
        when(materieRepo.findByNume("Matematica")).thenReturn(Optional.of(materie));
        when(catalogRepo.findByStudentCodAndMaterieCod("S1", "M1")).thenReturn(Optional.empty());
        when(catalogRepo.save(any(CatalogStudentMaterie.class))).thenAnswer(i -> i.getArgument(0));

        var result = service.addOrUpdate(dto);
        assertEquals(6.0, result.getNota());
        assertEquals(MaterieStatus.ACTIV, result.getStatus());
        verify(catalogRepo).save(any());
    }

    @Test
    void testAddOrUpdate_UpdateExisting() throws Exception {
        CatalogStudentMaterie existing = new CatalogStudentMaterie();
        existing.setStudent(student);
        existing.setMaterie(materie);
        existing.setNota(3.0);
        existing.setStatus(MaterieStatus.ACTIV);

        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        dto.setStudentCod("S1");
        dto.setNumeMaterie("Matematica");
        dto.setNota(4.0);

        when(studentRepo.findByCod("S1")).thenReturn(Optional.of(student));
        when(materieRepo.findByNume("Matematica")).thenReturn(Optional.of(materie));
        when(catalogRepo.findByStudentCodAndMaterieCod("S1", "M1")).thenReturn(Optional.of(existing));
        when(catalogRepo.save(existing)).thenReturn(existing);

        var result = service.addOrUpdate(dto);
        assertEquals(4.0, result.getNota());
        assertEquals(MaterieStatus.PICATA, result.getStatus());
        verify(catalogRepo).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        when(catalogRepo.findByStudentCodAndMaterieCod(any(), any())).thenReturn(Optional.empty());
        assertThrows(CatalogStudentMaterieNotFoundException.class,
                () -> service.update("S1", "M1", dto));
    }

    @Test
    void testDeleteByStudentAndMaterie_Success() throws Exception {
        CatalogStudentMaterie entry = new CatalogStudentMaterie();
        when(catalogRepo.findByStudentCodAndMaterieCod("S1", "M1")).thenReturn(Optional.of(entry));
        service.deleteByStudentAndMaterie("S1", "M1");
        verify(catalogRepo).delete(entry);
    }

    @Test
    void testDeleteByStudentAndMaterie_NotFound() {
        when(catalogRepo.findByStudentCodAndMaterieCod("S1", "M1")).thenReturn(Optional.empty());
        assertThrows(CatalogStudentMaterieNotFoundException.class,
                () -> service.deleteByStudentAndMaterie("S1", "M1"));
    }

    @Test
    void testDeleteById_NotFound() {
        when(catalogRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CatalogStudentMaterieNotFoundException.class,
                () -> service.delete(1));
    }
}