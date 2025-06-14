package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolTest {

    private Rol rol;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = mock(User.class);
        user2 = mock(User.class);

        rol = new Rol();
        rol.setId(1);
        rol.setName("STUDENT");
        rol.setUsers(List.of(user1, user2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, rol.getId());
        assertEquals("STUDENT", rol.getName());
        assertNotNull(rol.getUsers());
        assertEquals(2, rol.getUsers().size());
        assertTrue(rol.getUsers().contains(user1));
    }

    @Test
    void testConstructorWithName() {
        Rol newRol = new Rol("ADMIN");
        assertEquals("ADMIN", newRol.getName());
    }

    @Test
    void testUpdateName() {
        rol.setName("PROFESOR");
        assertEquals("PROFESOR", rol.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Rol r1 = new Rol();
        r1.setId(5);

        Rol r2 = new Rol();
        r2.setId(5);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNullFields() {
        Rol r = new Rol();
        assertEquals(0, r.getId()); // int default
        assertNull(r.getName());
        assertNull(r.getUsers());
    }
}
