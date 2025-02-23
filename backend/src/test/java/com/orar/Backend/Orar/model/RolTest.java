package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    private Rol rol;
    private List<User> users;

    @BeforeEach
    void setUp() {
        rol = new Rol("Student");
        rol.setId(1);
        rol.setName("Admin");

        users = new ArrayList<>();
        rol.setUsers(users);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, rol.getId());
        assertEquals("Admin", rol.getName());
        assertEquals(users, rol.getUsers());
    }

    @Test
    void testSetName() {
        rol.setName("Profesor");
        assertEquals("Profesor", rol.getName());
    }

    @Test
    void testSetUsers() {
        List<User> newUsers = new ArrayList<>();
        rol.setUsers(newUsers);
        assertEquals(newUsers, rol.getUsers());
    }
}
