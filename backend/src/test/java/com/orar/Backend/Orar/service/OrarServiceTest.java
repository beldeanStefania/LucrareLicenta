package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.dto.OrarDetailsDTO;
import com.orar.Backend.Orar.exception.OrarAlreadyExistsException;
import com.orar.Backend.Orar.exception.OrarNotFoundException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.model.Sala;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.OrarRepository;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RepartizareProfRepository;
import com.orar.Backend.Orar.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrarServiceTest {

    @Mock
    private OrarRepository orarRepo;
    @Mock
    private RepartizareProfRepository repartRepo;
    @Mock
    private SalaRepository salaRepo;
    @Mock
    private ProfesorRepository profesorRepo;
    @Mock
    private MaterieRepository materieRepo;

    @InjectMocks
    private OrarService service;

    private RepartizareProf rep;
    private Sala sala;
    private Orar orar1;
    private Orar orar2;

    @BeforeEach
    void setup() {
        Profesor prof = new Profesor();
        prof.setId(1);
        prof.setNume("Popescu");
        prof.setPrenume("Ion");

        Materie mat = new Materie();
        mat.setId(2);
        mat.setNume("Matematica");

        rep = new RepartizareProf();
        rep.setId(3);
        rep.setProfesor(prof);
        rep.setMaterie(mat);
        rep.setTip("Curs");

        sala = new Sala();
        sala.setId(4);
        sala.setNume("Sala A");

        orar1 = new Orar();
        orar1.setId(10);
        orar1.setGrupa("G1");
        orar1.setZi("Marti");
        orar1.setOraInceput(8);
        orar1.setOraSfarsit(10);
        orar1.setRepartizareProf(rep);
        orar1.setSala(sala);
        orar1.setFrecventa("F1");
        orar1.setFormatia("Fmt1");

        orar2 = new Orar();
        orar2.setId(11);
        orar2.setGrupa("G1");
        orar2.setZi("Luni");
        orar2.setOraInceput(10);
        orar2.setOraSfarsit(12);
        orar2.setRepartizareProf(rep);
        orar2.setSala(sala);
        orar2.setFrecventa("F2");
        orar2.setFormatia("Fmt2");
    }

    @Test
    @DisplayName("getOrarByGrupa should delegate to repository")
    void testGetOrarByGrupa() {
        when(orarRepo.findByGrupa("G1")).thenReturn(List.of(orar1));
        var result = service.getOrarByGrupa("G1");

        assertEquals(1, result.size());
        assertSame(orar1, result.get(0));
        verify(orarRepo).findByGrupa("G1");
    }

    @Test
    @DisplayName("getOrarDetailsByGrupa should map and sort entries")
    void testGetOrarDetailsByGrupa() {
        when(orarRepo.findByGrupa("G1")).thenReturn(List.of(orar1, orar2));

        var details = service.getOrarDetailsByGrupa("G1");
        assertEquals(List.of("Luni", "Marti"), details.stream().map(OrarDetailsDTO::getZi).toList());
        var first = details.get(0);
        assertEquals("Fmt2", first.getFormatia());
        assertEquals("Sala A", first.getSala());
        assertEquals("Matematica", first.getDisciplina());
        assertEquals("Curs", first.getTipul());
        assertEquals("Popescu Ion", first.getCadruDidactic());
        assertEquals("F2", first.getFrecventa());
    }

    @Test
    @DisplayName("add should create and save schedule twice (build & final)")
    void testAddSuccess() throws Exception {
        var dto = new OrarDTO();
        dto.setGrupa("G1"); dto.setZi("Luni");
        dto.setOraInceput(8); dto.setOraSfarsit(10);
        dto.setRepartizareProfId(3); dto.setSalaId(4);
        dto.setSemigrupa("S1"); dto.setTip("Laborator");

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(repartRepo.findById(3)).thenReturn(Optional.of(rep));
        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        lenient().when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.add(dto);
        assertEquals("G1", result.getGrupa());
        assertEquals("Luni", result.getZi());
        assertEquals("MIR", result.getFormatia()); // repart tip is "Curs", so determineFormatia uses Curs -> "MIR"
        verify(orarRepo, times(2)).save(any(Orar.class));
    }

    @Test
    @DisplayName("add should throw when time slot overlaps")
    void testAddThrowsOverlap() {
        var dto = new OrarDTO();
        dto.setGrupa("G1"); dto.setZi("Luni");
        dto.setOraInceput(8); dto.setOraSfarsit(10);
        dto.setRepartizareProfId(3); dto.setSalaId(4);

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(new Orar()));
        assertThrows(OrarAlreadyExistsException.class, () -> service.add(dto));
    }

    @Test
    @DisplayName("add should throw IllegalArgumentException if grupa missing")
    void testAddThrowsMissingGrupa() {
        var dto = new OrarDTO();
        dto.setGrupa(null);
        dto.setRepartizareProfId(3); dto.setSalaId(4);

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> service.add(dto));
    }

    @Test
    @DisplayName("add should throw NoSuchElementException if repart absent")
    void testAddThrowsRepartNotFound() {
        var dto = new OrarDTO();
        dto.setGrupa("G1"); dto.setZi("Luni");
        dto.setOraInceput(8); dto.setOraSfarsit(10);
        dto.setRepartizareProfId(99); dto.setSalaId(4);

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(repartRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.add(dto));
    }

    @Test
    @DisplayName("deleteOrar should throw when not found")
    void testDeleteOrarNotFound() {
        when(orarRepo.findById(50)).thenReturn(Optional.empty());
        assertThrows(OrarNotFoundException.class, () -> service.deleteOrar(50));
    }

    @Test
    @DisplayName("deleteOrar should succeed when found")
    void testDeleteOrarSuccess() throws Exception {
        when(orarRepo.findById(10)).thenReturn(Optional.of(orar1));
        service.deleteOrar(10);
        verify(orarRepo).findById(10);
    }

    @Test
    @DisplayName("getOrarDetailsByProfesor should map & sort")
    void testGetOrarDetailsByProfesor() {
        when(orarRepo.findByRepartizareProf_Profesor_Id(1)).thenReturn(List.of(orar1, orar2));
        var list = service.getOrarDetailsByProfesor(1);
        assertEquals(List.of("Luni", "Marti"), list.stream().map(OrarDetailsDTO::getZi).toList());
    }

    @Test
    @DisplayName("updateOrar should throw if not found")
    void testUpdateOrarNotFound() {
        when(orarRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(OrarNotFoundException.class, () -> service.updateOrar(99, new OrarDTO()));
    }

    @Test
    @DisplayName("updateOrar should save twice and return original")
    void testUpdateOrarSuccess() throws Exception {
        when(orarRepo.findById(10)).thenReturn(Optional.of(orar1));
        var dto = new OrarDTO();
        dto.setGrupa("G1"); dto.setZi("Luni");
        dto.setOraInceput(8); dto.setOraSfarsit(10);
        dto.setRepartizareProfId(3); dto.setSalaId(4);
        dto.setSemigrupa("S1"); dto.setTip("Laborator");

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(repartRepo.findById(3)).thenReturn(Optional.of(rep));
        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        lenient().when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.updateOrar(10, dto);
        assertSame(orar1, result);
        verify(orarRepo, times(2)).save(any(Orar.class));
    }
}
