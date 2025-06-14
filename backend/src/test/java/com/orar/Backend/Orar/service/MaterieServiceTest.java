package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.exception.MaterieAlreadyExistsException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.MaterieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterieServiceTest {

    @Mock
    private MaterieRepository materieRepo;

    @InjectMocks
    private MaterieService service;

    private MaterieDTO dto;
    private Materie existing;

    @BeforeEach
    void setup() {
        dto = new MaterieDTO();
        dto.setNume("Algebra");
        dto.setSemestru(3);
        dto.setCod("ALG101");
        dto.setCredite(5);

        existing = new Materie();
        existing.setId(1);
        existing.setNume("Algebra");
        existing.setSemestru(2);
        existing.setCod("ALG101");
        existing.setCredite(4);
    }

    @Test
    @DisplayName("getAll returns all Materie")
    void testGetAll() {
        when(materieRepo.findAll()).thenReturn(List.of(existing));
        List<Materie> result = service.getAll();
        assertEquals(1, result.size());
        assertSame(existing, result.get(0));
        verify(materieRepo).findAll();
    }

    @Test
    @DisplayName("add should save new Materie")
    void testAddSuccess() throws Exception {
        when(materieRepo.findByCod("ALG101")).thenReturn(Optional.empty());
        ArgumentCaptor<Materie> cap = ArgumentCaptor.forClass(Materie.class);
        when(materieRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Materie result = service.add(dto);
        assertEquals("Algebra", result.getNume());
        assertEquals(3, result.getSemestru());
        assertEquals("ALG101", result.getCod());
        // semestru=3->an=2
        assertEquals(2, result.getAn());
        assertEquals(5, result.getCredite());
        verify(materieRepo).findByCod("ALG101");
        verify(materieRepo).save(cap.capture());
    }

    @Test
    @DisplayName("add throws when MaterieAlreadyExists")
    void testAddAlreadyExists() {
        when(materieRepo.findByCod("ALG101")).thenReturn(Optional.of(existing));
        assertThrows(MaterieAlreadyExistsException.class, () -> service.add(dto));
        verify(materieRepo).findByCod("ALG101");
        verify(materieRepo, never()).save(any());
    }

    @Test
    @DisplayName("createMaterie sets correct an for semestru 1-2")
    void testCreateAnFirstYear() {
        dto.setSemestru(2);
        Materie m = service.createMaterie(dto);
        assertEquals(1, m.getAn());
    }

    @Test
    @DisplayName("createMaterie sets correct an for semestru 5")
    void testCreateAnThirdYear() {
        dto.setSemestru(5);
        Materie m = service.createMaterie(dto);
        assertEquals(3, m.getAn());
    }

    @Test
    void testUpdateSuccess() throws Exception {
        when(materieRepo.findByNumeAndCod("Algebra", "ALG101")).thenReturn(Optional.of(existing));
        when(materieRepo.save(existing)).thenReturn(existing);

        Materie updated = service.update(dto);
        assertSame(existing, updated);
        assertEquals("Algebra", existing.getNume());
        verify(materieRepo).findByNumeAndCod("Algebra", "ALG101");
        verify(materieRepo).save(existing);
    }

    @Test
    @DisplayName("update throws when not found")
    void testUpdateNotFound() {
        when(materieRepo.findByNumeAndCod("Algebra", "ALG101")).thenReturn(Optional.empty());
        assertThrows(MaterieNotFoundException.class, () -> service.update(dto));
        verify(materieRepo).findByNumeAndCod("Algebra", "ALG101");
    }

    @Test
    @DisplayName("delete removes when exists")
    void testDeleteSuccess() throws Exception {
        when(materieRepo.findById(1)).thenReturn(Optional.of(existing));
        service.delete(1);
        verify(materieRepo).delete(existing);
    }

    @Test
    @DisplayName("delete throws when not found")
    void testDeleteNotFound() {
        when(materieRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(MaterieNotFoundException.class, () -> service.delete(1));
    }

    @Test
    @DisplayName("importFromCsv throws RuntimeException on IO error")
    void testImportIOException() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("bad"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.importFromCsv(file));
        assertTrue(ex.getMessage().contains("Nu am putut citi fișierul"));
    }}
