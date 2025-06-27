package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.dto.OrarDetailsDTO;
import com.orar.Backend.Orar.exception.GrupaNotFoundException;
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

    @Test
    @DisplayName("getAll should return all orar entries")
    void testGetAll() {
        when(orarRepo.findAll()).thenReturn(List.of(orar1, orar2));

        var result = service.getAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(orar1));
        assertTrue(result.contains(orar2));
        verify(orarRepo).findAll();
    }

    @Test
    @DisplayName("getOrarById should return orar when found")
    void testGetOrarByIdFound() {
        when(orarRepo.findById(10)).thenReturn(Optional.of(orar1));

        var result = service.getOrarById(10);
        assertEquals(Optional.of(orar1), result); // Service returns Optional
        verify(orarRepo).findById(10);
    }

    @Test
    @DisplayName("getOrarById should return empty when not found")
    void testGetOrarByIdNotFound() {
        when(orarRepo.findById(99)).thenReturn(Optional.empty());

        var result = service.getOrarById(99);
        assertEquals(Optional.empty(), result); // Service returns Optional.empty(), doesn't throw
        verify(orarRepo).findById(99);
    }

    @Test
    @DisplayName("compareDays should sort days correctly")
    void testCompareDays() {
        // This tests the private method indirectly through sorting
        when(orarRepo.findByGrupa("G1")).thenReturn(List.of(orar1, orar2)); // Marti, Luni

        var details = service.getOrarDetailsByGrupa("G1");
        // Should be sorted: Luni before Marti
        assertEquals("Luni", details.get(0).getZi());
        assertEquals("Marti", details.get(1).getZi());
    }

    @Test
    @DisplayName("determineFormatia should handle different scenarios with repartizare types")
    void testDetermineFormatia() throws Exception {
        // Create different repartizare objects with different tips
        RepartizareProf laboratorRep = new RepartizareProf();
        laboratorRep.setId(10);
        laboratorRep.setProfesor(rep.getProfesor());
        laboratorRep.setMaterie(rep.getMaterie());
        laboratorRep.setTip("Laborator");

        RepartizareProf seminarRep = new RepartizareProf();
        seminarRep.setId(11);
        seminarRep.setProfesor(rep.getProfesor());
        seminarRep.setMaterie(rep.getMaterie());
        seminarRep.setTip("Seminar");

        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        // Test with Laborator repartizare tip and semigrupa "1"
        when(repartRepo.findById(10)).thenReturn(Optional.of(laboratorRep));
        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Luni");
        dto.setSemigrupa("1");
        dto.setRepartizareProfId(10);
        dto.setSalaId(4);

        var result = service.add(dto);
        assertEquals("214/1", result.getFormatia()); // Laborator + semigrupa "1"

        // Test with Seminar repartizare tip
        when(repartRepo.findById(11)).thenReturn(Optional.of(seminarRep));
        dto.setRepartizareProfId(11);
        result = service.add(dto);
        assertEquals("214", result.getFormatia()); // Seminar uses grupa only

        // Test with Curs repartizare tip (original rep)
        when(repartRepo.findById(3)).thenReturn(Optional.of(rep));
        dto.setRepartizareProfId(3);
        result = service.add(dto);
        assertEquals("MIR", result.getFormatia()); // Curs -> "MIR"
    }

    @Test
    @DisplayName("determineFormatia edge cases")
    void testDetermineFormatiaEdgeCases() throws Exception {
        // Since rep has tip "Curs", all tests will result in "MIR"
        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Luni"); // Add required field
        dto.setSemigrupa("unknown");
        dto.setTip("Laborator");
        dto.setRepartizareProfId(3);
        dto.setSalaId(4);

        when(repartRepo.findById(3)).thenReturn(Optional.of(rep));
        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        // Test with unknown semigrupa - rep tip is "Curs" so result is "MIR"
        var result = service.add(dto);
        assertEquals("MIR", result.getFormatia()); // Curs -> "MIR"

        // Test with null semigrupa - rep tip is still "Curs"
        dto.setSemigrupa(null);
        result = service.add(dto);
        assertEquals("MIR", result.getFormatia()); // Curs -> "MIR"

        // Test with laborator repartizare tip
        RepartizareProf laboratorRep = new RepartizareProf();
        laboratorRep.setId(12);
        laboratorRep.setProfesor(rep.getProfesor());
        laboratorRep.setMaterie(rep.getMaterie());
        laboratorRep.setTip("Laborator");

        when(repartRepo.findById(12)).thenReturn(Optional.of(laboratorRep));
        dto.setRepartizareProfId(12);
        dto.setSemigrupa("unknown");

        result = service.add(dto);
        assertEquals("214/unknown", result.getFormatia()); // Laborator with unknown semigrupa -> grupa/semigrupa
    }

    @Test
    @DisplayName("createRepartizareProfIfNotExists flow - test with profesorId and materie")
    void testCreateRepartizareProfIfNotExistsFlow() throws Exception {
        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Luni");
        dto.setOraInceput(8);
        dto.setOraSfarsit(10);
        dto.setSalaId(4);
        dto.setProfesorId(1); // Use existing field
        dto.setMaterie("Matematica"); // Use existing field
        dto.setTip("Curs"); // Use existing field

        when(profesorRepo.findById(1)).thenReturn(Optional.of(rep.getProfesor()));
        when(materieRepo.findByNume("Matematica")).thenReturn(Optional.of(rep.getMaterie()));
        when(repartRepo.findByProfesorAndMaterieAndTip(any(), any(), any())).thenReturn(Optional.of(rep));

        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.add(dto);

        assertNotNull(result);
        assertEquals("MIR", result.getFormatia()); // Curs -> MIR
    }

    @Test
    @DisplayName("checkOrarExists should detect overlap correctly")
    void testCheckOrarExistsDetectsOverlap() {
        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Luni");
        dto.setOraInceput(8);
        dto.setOraSfarsit(10);
        dto.setSalaId(4);
        dto.setRepartizareProfId(3);

        // Mock overlapping schedule
        Orar overlapping = new Orar();
        when(orarRepo.findOverlappingOrar(4, "Luni", 8, 10)).thenReturn(List.of(overlapping));

        assertThrows(OrarAlreadyExistsException.class, () -> service.add(dto));
    }

    @Test
    @DisplayName("add should handle missing sala gracefully")
    void testAddWithMissingSala() {
        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Luni");
        dto.setOraInceput(8);
        dto.setOraSfarsit(10);
        dto.setRepartizareProfId(3);
        dto.setSalaId(999);

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(repartRepo.findById(3)).thenReturn(Optional.of(rep));
        when(salaRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.add(dto));
    }

    @Test
    @DisplayName("buildOrar should correctly build orar from DTO")
    void testBuildOrar() throws Exception {
        // Create a repartizare with Seminar tip for this test
        RepartizareProf seminarRep = new RepartizareProf();
        seminarRep.setId(5);
        seminarRep.setProfesor(rep.getProfesor());
        seminarRep.setMaterie(rep.getMaterie());
        seminarRep.setTip("Seminar");

        var dto = new OrarDTO();
        dto.setGrupa("214");
        dto.setZi("Miercuri");
        dto.setOraInceput(14);
        dto.setOraSfarsit(16);
        dto.setRepartizareProfId(5);
        dto.setSalaId(4);
        dto.setFrecventa("Saptamanal");
        dto.setTip("Seminar");
        dto.setSemigrupa("1");

        when(orarRepo.findOverlappingOrar(anyInt(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(repartRepo.findById(5)).thenReturn(Optional.of(seminarRep));
        when(salaRepo.findById(4)).thenReturn(Optional.of(sala));
        when(orarRepo.save(any(Orar.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.add(dto);

        assertEquals("214", result.getGrupa());
        assertEquals("Miercuri", result.getZi());
        assertEquals(14, result.getOraInceput());
        assertEquals(16, result.getOraSfarsit());
        assertEquals(seminarRep, result.getRepartizareProf());
        assertEquals(sala, result.getSala());
        assertEquals("Saptamanal", result.getFrecventa());
        assertEquals("214", result.getFormatia()); // Seminar -> grupa only
    }

    @Test
    @DisplayName("test various day sorting scenarios")
    void testComplexDaySorting() {
        // Create orar entries for different days
        Orar orarVineri = new Orar();
        orarVineri.setZi("Vineri");
        orarVineri.setOraInceput(8);
        orarVineri.setRepartizareProf(rep);
        orarVineri.setSala(sala);

        Orar orarMiercuri = new Orar();
        orarMiercuri.setZi("Miercuri");
        orarMiercuri.setOraInceput(10);
        orarMiercuri.setRepartizareProf(rep);
        orarMiercuri.setSala(sala);

        when(orarRepo.findByGrupa("TestGroup")).thenReturn(List.of(orarVineri, orarMiercuri, orar1, orar2));

        var details = service.getOrarDetailsByGrupa("TestGroup");

        // Should be sorted by day order: Luni, Marti, Miercuri, Vineri
        var days = details.stream().map(OrarDetailsDTO::getZi).toList();
        assertEquals(List.of("Luni", "Marti", "Miercuri", "Vineri"), days);
    }
}
