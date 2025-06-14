package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RepartizareProfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepartizareProfServiceTest {

    @Mock
    private RepartizareProfRepository repartizareProfRepo;

    @Mock
    private ProfesorRepository profesorRepo;

    @Mock
    private MaterieRepository materieRepo;

    @InjectMocks
    private RepartizareProfService service;

    private Profesor sampleProfesor;
    private Materie sampleMaterie;
    private RepartizareProfDTO dto;

    @BeforeEach
    void setup() {
        sampleProfesor = new Profesor();
        sampleProfesor.setId(1);
        sampleProfesor.setNume("Popescu");
        sampleProfesor.setPrenume("Ion");

        sampleMaterie = new Materie();
        sampleMaterie.setId(2);
        sampleMaterie.setNume("Matematica");

        dto = new RepartizareProfDTO();
        dto.setNumeProfesor("Popescu");
        dto.setPrenumeProfesor("Ion");
        dto.setMaterie("Matematica");
        dto.setTip("Curs");
    }

    @Test
    @DisplayName("getAll should return all records from repository")
    void testGetAll() {
        RepartizareProf rp = new RepartizareProf();
        when(repartizareProfRepo.findAll()).thenReturn(List.of(rp));

        List<RepartizareProf> result = service.getAll();
        assertEquals(1, result.size());
        assertSame(rp, result.get(0));
        verify(repartizareProfRepo).findAll();
    }


    @Nested
    @DisplayName("delete method tests")
    class DeleteTests {
        @Test
        @DisplayName("Successfully deletes existing repartizare")
        void testDeleteSuccess() {
            RepartizareProf rp = new RepartizareProf();
            when(repartizareProfRepo.findById(1)).thenReturn(Optional.of(rp));

            service.delete(1);
            verify(repartizareProfRepo).delete(rp);
        }

        @Test
        @DisplayName("Throws if repartizare not found")
        void testDeleteNotFound() {
            when(repartizareProfRepo.findById(1)).thenReturn(Optional.empty());
            assertThrows(RuntimeException.class, () -> service.delete(1));
        }
    }

    @Nested
    @DisplayName("updateRepartizareProf tests")
    class UpdateTests {
        @Test
        @DisplayName("Successfully updates an existing repartizare")
        void testUpdateSuccess() throws Exception {
            RepartizareProf existing = new RepartizareProf();
            existing.setId(5);
            when(repartizareProfRepo.findById(5)).thenReturn(Optional.of(existing));
            when(materieRepo.findByNume("Matematica")).thenReturn(Optional.of(sampleMaterie));
            when(profesorRepo.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.of(sampleProfesor));
            when(repartizareProfRepo.findByProfesorAndMaterieAndTip(any(), any(), any()))
                    .thenReturn(Optional.empty());
            when(repartizareProfRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            RepartizareProf result = service.updateRepartizareProf(5, dto);
            assertEquals(5, result.getId());
            verify(repartizareProfRepo).save(existing);
        }

        @Test
        @DisplayName("Throws if repartizare to update not found")
        void testUpdateNotFound() {
            when(repartizareProfRepo.findById(5)).thenReturn(Optional.empty());
            assertThrows(RuntimeException.class, () -> service.updateRepartizareProf(5, dto));
        }
    }

    @Nested
    @DisplayName("getMateriiProfesor and getMateriiDistincteProfesor tests")
    class QueryTests {
        @Test
        @DisplayName("Returns mapped DTOs for a professor")
        void testGetMateriiProfesor() throws Exception {
            when(profesorRepo.findById(1)).thenReturn(Optional.of(sampleProfesor));
            RepartizareProf r1 = new RepartizareProf();
            r1.setId(10);
            r1.setMaterie(sampleMaterie);
            r1.setProfesor(sampleProfesor);
            r1.setTip("Curs");
            when(repartizareProfRepo.findByProfesor(sampleProfesor)).thenReturn(List.of(r1));

            List<RepartizareProfDTO> dtos = service.getMateriiProfesor(1);
            assertEquals(1, dtos.size());
            RepartizareProfDTO out = dtos.get(0);
            assertEquals(10, out.getId());
            assertEquals("Matematica", out.getMaterie());
            assertEquals("Curs", out.getTip());
            assertEquals("Popescu", out.getNumeProfesor());
            assertEquals("Ion", out.getPrenumeProfesor());
        }

        @Test
        @DisplayName("Throws if professor not found in getMateriiProfesor")
        void testGetMateriiProfesorNotFound() {
            when(profesorRepo.findById(2)).thenReturn(Optional.empty());
            assertThrows(ProfesorNotFoundException.class, () -> service.getMateriiProfesor(2));
        }

        @Test
        @DisplayName("Returns distinct materia names for a professor")
        void testGetMateriiDistincteProfesor() throws Exception {
            when(profesorRepo.findById(1)).thenReturn(Optional.of(sampleProfesor));
            RepartizareProf rA = new RepartizareProf();
            rA.setMaterie(sampleMaterie);
            RepartizareProf rB = new RepartizareProf();
            rB.setMaterie(sampleMaterie);
            when(repartizareProfRepo.findByProfesor(sampleProfesor)).thenReturn(List.of(rA, rB));

            List<String> names = service.getMateriiDistincteProfesor(1);
            assertEquals(1, names.size());
            assertEquals("Matematica", names.get(0));
        }

        @Test
        @DisplayName("Throws if professor not found in getMateriiDistincteProfesor")
        void testGetMateriiDistincteProfesorNotFound() {
            when(profesorRepo.findById(3)).thenReturn(Optional.empty());
            assertThrows(ProfesorNotFoundException.class, () -> service.getMateriiDistincteProfesor(3));
        }
    }
}
