package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.exception.CladireAlreadyExistsException;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.repository.CladireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CladireServiceTest {

    @Mock
    private CladireRepository repository;

    @InjectMocks
    private CladireService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Cladire c1 = new Cladire(); c1.setNume("A");
        Cladire c2 = new Cladire(); c2.setNume("B");
        when(repository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cladire> all = service.getAll();
        assertEquals(2, all.size());
        verify(repository).findAll();
    }

    @Test
    void testAdd_Success() throws CladireAlreadyExistsException {
        CladireDTO dto = new CladireDTO("X", "Addr");
        when(repository.findByNume("X")).thenReturn(Optional.empty());
        Cladire saved = new Cladire(); saved.setNume("X"); saved.setAdresa("Addr");
        when(repository.save(any(Cladire.class))).thenReturn(saved);

        Cladire result = service.add(dto);
        assertEquals("X", result.getNume());
        assertEquals("Addr", result.getAdresa());
        verify(repository).findByNume("X");
        verify(repository).save(any());
    }

    @Test
    void testAdd_AlreadyExists() {
        CladireDTO dto = new CladireDTO("X", "Addr");
        when(repository.findByNume("X")).thenReturn(Optional.of(new Cladire()));
        assertThrows(CladireAlreadyExistsException.class, () -> service.add(dto));
        verify(repository).findByNume("X");
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdate_Success() throws CladireNotFoundException {
        CladireDTO dto = new CladireDTO("Y", "NewAddr");
        Cladire existing = new Cladire(); existing.setNume("Old"); existing.setAdresa("OldAddr");
        when(repository.findByNume("Old")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Cladire result = service.update("Old", dto);
        assertEquals("Y", result.getNume());
        assertEquals("NewAddr", result.getAdresa());
        verify(repository).findByNume("Old");
        verify(repository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        CladireDTO dto = new CladireDTO("Y", "NewAddr");
        when(repository.findByNume("Old")).thenReturn(Optional.empty());
        assertThrows(CladireNotFoundException.class, () -> service.update("Old", dto));
        verify(repository).findByNume("Old");
    }

    @Test
    void testDelete_Success() throws CladireNotFoundException {
        Cladire existing = new Cladire(); existing.setNume("Z");
        when(repository.findByNume("Z")).thenReturn(Optional.of(existing));

        service.delete("Z");
        verify(repository).findByNume("Z");
        verify(repository).delete(existing);
    }

    @Test
    void testDelete_NotFound() {
        when(repository.findByNume("Z")).thenReturn(Optional.empty());
        assertThrows(CladireNotFoundException.class, () -> service.delete("Z"));
        verify(repository).findByNume("Z");
        verify(repository, never()).delete(any());
    }
}
