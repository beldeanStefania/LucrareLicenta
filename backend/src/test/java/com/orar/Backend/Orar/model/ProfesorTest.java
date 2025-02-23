package com.orar.Backend.Orar.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class ProfesorTest {

    private Profesor profesor;
    private User user;
    private List<RepartizareProf> repartizareProfs;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNume("Ionescu");
        profesor.setPrenume("Mihai");

        user = Mockito.mock(User.class);
        profesor.setUser(user);

        repartizareProfs = new ArrayList<>();
        profesor.setRepartizareProfs(repartizareProfs);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, profesor.getId());
        assertEquals("Ionescu", profesor.getNume());
        assertEquals("Mihai", profesor.getPrenume());
        assertEquals(user, profesor.getUser());
        assertEquals(repartizareProfs, profesor.getRepartizareProfs());
    }

    @Test
    void testSetNume() {
        profesor.setNume("Popescu");
        assertEquals("Popescu", profesor.getNume());
    }

    @Test
    void testSetPrenume() {
        profesor.setPrenume("Ion");
        assertEquals("Ion", profesor.getPrenume());
    }

    @Test
    void testSetUser() {
        User newUser = Mockito.mock(User.class);
        profesor.setUser(newUser);
        assertEquals(newUser, profesor.getUser());
    }

    @Test
    void testSetRepartizareProfs() {
        List<RepartizareProf> newRepartizareProfs = new ArrayList<>();
        profesor.setRepartizareProfs(newRepartizareProfs);
        assertEquals(newRepartizareProfs, profesor.getRepartizareProfs());
    }
}