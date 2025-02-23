package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class UserTest {

    private User user;
    private Rol role;
    private Student student;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("testPassword");

        role = Mockito.mock(Rol.class);
        user.setRole(role);

        student = Mockito.mock(Student.class);
        user.setStudent(student);

        profesor = Mockito.mock(Profesor.class);
        user.setProfesor(profesor);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(student, user.getStudent());
        assertEquals(profesor, user.getProfesor());
    }

    @Test
    void testSetUsername() {
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testSetRole() {
        Rol newRole = Mockito.mock(Rol.class);
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testSetStudent() {
        Student newStudent = Mockito.mock(Student.class);
        user.setStudent(newStudent);
        assertEquals(newStudent, user.getStudent());
    }

    @Test
    void testSetProfesor() {
        Profesor newProfesor = Mockito.mock(Profesor.class);
        user.setProfesor(newProfesor);
        assertEquals(newProfesor, user.getProfesor());
    }
}
