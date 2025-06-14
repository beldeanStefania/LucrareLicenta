package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.SalaDTO;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.exception.SalaAlreadyExistsException;
import com.orar.Backend.Orar.exception.SalaNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.repository.CladireRepository;
import com.orar.Backend.Orar.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @InjectMocks
    private SalaService salaService;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private CladireRepository cladireRepository;

    private SalaDTO salaDTO;
    private Cladire cladire;
    private Sala sala;

    @BeforeEach
    void setup() {
        salaDTO = new SalaDTO();
        salaDTO.setNume("Sala A");
        salaDTO.setCapacitate(30);
        salaDTO.setNumeCladire("Cladire 1");

        cladire = new Cladire();

        sala = new Sala();
        sala.setNume("Sala A");
        sala.setCapacitate(30);
        sala.setCladire(cladire);
    }

    @Test
    void testAdd_NewSala_Success() throws Exception {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.empty());
        when(cladireRepository.findByNume("Cladire 1")).thenReturn(Optional.of(cladire));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        Sala result = salaService.add(salaDTO);

        assertNotNull(result);
        assertEquals("Sala A", result.getNume());
    }

    @Test
    void testAdd_SalaAlreadyExists() {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.of(sala));
        assertThrows(SalaAlreadyExistsException.class, () -> salaService.add(salaDTO));
    }

    @Test
    void testAdd_CladireNotFound() {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.empty());
        when(cladireRepository.findByNume("Cladire 1")).thenReturn(Optional.empty());
        assertThrows(CladireNotFoundException.class, () -> salaService.add(salaDTO));
    }

    @Test
    void testUpdate_SalaFound() throws Exception {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.of(sala));
        when(cladireRepository.findByNume("Cladire 1")).thenReturn(Optional.of(cladire));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        Sala result = salaService.update("Sala A", salaDTO);
        assertNotNull(result);
        assertEquals("Sala A", result.getNume());
    }

    @Test
    void testUpdate_SalaNotFound() {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.empty());
        assertThrows(SalaNotFoundException.class, () -> salaService.update("Sala A", salaDTO));
    }

    @Test
    void testDelete_SalaFound() throws SalaNotFoundException {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.of(sala));
        salaService.delete("Sala A");
        verify(salaRepository).delete(sala);
    }

    @Test
    void testDelete_SalaNotFound() {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.empty());
        assertThrows(SalaNotFoundException.class, () -> salaService.delete("Sala A"));
    }

    @Test
    void testGetSalaByNume_Found() throws SalaNotFoundException {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.of(sala));
        Sala result = salaService.getSalaByNume("Sala A");
        assertNotNull(result);
    }

    @Test
    void testGetSalaByNume_NotFound() {
        when(salaRepository.findByNume("Sala A")).thenReturn(Optional.empty());
        assertThrows(SalaNotFoundException.class, () -> salaService.getSalaByNume("Sala A"));
    }

    @Test
    void testGetAll() {
        when(salaRepository.findAll()).thenReturn(List.of(sala));
        List<Sala> result = salaService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetSaliByCladire() {
        when(salaRepository.findByCladireId(1)).thenReturn(List.of(sala));
        List<Sala> result = salaService.getSaliByCladire(1);
        assertEquals(1, result.size());
    }
}
