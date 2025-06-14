package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepartizareProfTest {

    private RepartizareProf repartizareProf;
    private Profesor profesor;
    private Materie materie;
    private Orar orar1, orar2;

    @BeforeEach
    void setUp() {
        profesor = mock(Profesor.class);
        materie = mock(Materie.class);
        orar1 = mock(Orar.class);
        orar2 = mock(Orar.class);

        repartizareProf = new RepartizareProf();
        repartizareProf.setId(1);
        repartizareProf.setTip("Curs");
        repartizareProf.setProfesor(profesor);
        repartizareProf.setMaterie(materie);
        repartizareProf.setOrar(List.of(orar1, orar2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, repartizareProf.getId());
        assertEquals("Curs", repartizareProf.getTip());
        assertEquals(profesor, repartizareProf.getProfesor());
        assertEquals(materie, repartizareProf.getMaterie());
        assertEquals(2, repartizareProf.getOrar().size());
    }

    @Test
    void testSetTip() {
        repartizareProf.setTip("Laborator");
        assertEquals("Laborator", repartizareProf.getTip());
    }

    @Test
    void testSetProfesor() {
        Profesor newProf = mock(Profesor.class);
        repartizareProf.setProfesor(newProf);
        assertEquals(newProf, repartizareProf.getProfesor());
    }

    @Test
    void testSetMaterie() {
        Materie newMaterie = mock(Materie.class);
        repartizareProf.setMaterie(newMaterie);
        assertEquals(newMaterie, repartizareProf.getMaterie());
    }

    @Test
    void testOrarInitialization() {
        RepartizareProf empty = new RepartizareProf();
        assertNotNull(empty.getOrar());
        assertTrue(empty.getOrar().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        RepartizareProf r1 = new RepartizareProf();
        r1.setId(10);
        RepartizareProf r2 = new RepartizareProf();
        r2.setId(10);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
