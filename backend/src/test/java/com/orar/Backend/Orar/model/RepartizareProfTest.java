package com.orar.Backend.Orar.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class RepartizareProfTest {

    private RepartizareProf repartizareProf;
    private Profesor profesor;
    private Materie materie;
    private List<Orar> orar;

    @BeforeEach
    void setUp() {
        repartizareProf = new RepartizareProf();
        repartizareProf.setId(1);
        repartizareProf.setTip("Curs");

        profesor = Mockito.mock(Profesor.class);
        repartizareProf.setProfesor(profesor);

        materie = Mockito.mock(Materie.class);
        repartizareProf.setMaterie(materie);

        orar = new ArrayList<>();
        repartizareProf.setOrar(orar);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, repartizareProf.getId());
        assertEquals("Curs", repartizareProf.getTip());
        assertEquals(profesor, repartizareProf.getProfesor());
        assertEquals(materie, repartizareProf.getMaterie());
        assertEquals(orar, repartizareProf.getOrar());
    }

    @Test
    void testSetTip() {
        repartizareProf.setTip("Seminar");
        assertEquals("Seminar", repartizareProf.getTip());
    }

    @Test
    void testSetProfesor() {
        Profesor newProfesor = Mockito.mock(Profesor.class);
        repartizareProf.setProfesor(newProfesor);
        assertEquals(newProfesor, repartizareProf.getProfesor());
    }

    @Test
    void testSetMaterie() {
        Materie newMaterie = Mockito.mock(Materie.class);
        repartizareProf.setMaterie(newMaterie);
        assertEquals(newMaterie, repartizareProf.getMaterie());
    }

    @Test
    void testSetOrar() {
        List<Orar> newOrar = new ArrayList<>();
        repartizareProf.setOrar(newOrar);
        assertEquals(newOrar, repartizareProf.getOrar());
    }
}