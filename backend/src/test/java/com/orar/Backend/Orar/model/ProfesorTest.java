package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfesorTest {

    private Profesor profesor;
    private User user;
    private RepartizareProf r1, r2;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        r1 = mock(RepartizareProf.class);
        r2 = mock(RepartizareProf.class);

        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNume("Popescu");
        profesor.setPrenume("Ion");
        profesor.setUser(user);
        profesor.setRepartizareProfs(List.of(r1, r2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, profesor.getId());
        assertEquals("Popescu", profesor.getNume());
        assertEquals("Ion", profesor.getPrenume());
        assertEquals(user, profesor.getUser());
        assertEquals(2, profesor.getRepartizareProfs().size());
    }

    @Test
    void testSetNumeAndPrenume() {
        profesor.setNume("Ionescu");
        profesor.setPrenume("Maria");
        assertEquals("Ionescu", profesor.getNume());
        assertEquals("Maria", profesor.getPrenume());
    }

    @Test
    void testSetUser() {
        User newUser = mock(User.class);
        profesor.setUser(newUser);
        assertEquals(newUser, profesor.getUser());
    }

    @Test
    void testSetRepartizareProfs() {
        RepartizareProf newR = mock(RepartizareProf.class);
        profesor.setRepartizareProfs(List.of(newR));
        assertEquals(1, profesor.getRepartizareProfs().size());
        assertEquals(newR, profesor.getRepartizareProfs().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        Profesor p1 = new Profesor();
        p1.setId(7);
        Profesor p2 = new Profesor();
        p2.setId(7);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
