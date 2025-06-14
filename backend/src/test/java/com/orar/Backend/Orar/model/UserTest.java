package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;
    private Rol role;
    private Student student;
    private Profesor profesor;
    private TodoItem todo1;
    private TodoItem todo2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");

        role = mock(Rol.class);
        student = mock(Student.class);
        profesor = mock(Profesor.class);

        user.setRole(role);
        user.setStudent(student);
        user.setProfesor(profesor);

        todo1 = new TodoItem();
        todo1.setDescription("Task 1");
        todo1.setUser(user);

        todo2 = new TodoItem();
        todo2.setDescription("Task 2");
        todo2.setUser(user);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(role, user.getRole());
        assertEquals(student, user.getStudent());
        assertEquals(profesor, user.getProfesor());
    }

    @Test
    void testSetAndGetTodoItems() {
        user.getTodoItems().add(todo1);
        user.getTodoItems().add(todo2);

        List<TodoItem> items = user.getTodoItems();
        assertEquals(2, items.size());
        assertTrue(items.contains(todo1));
        assertTrue(items.contains(todo2));
    }

    @Test
    void testTodoItemsInitialization() {
        assertNotNull(user.getTodoItems());
        assertTrue(user.getTodoItems().isEmpty());
    }

    @Test
    void testUpdateFields() {
        user.setUsername("newUser");
        user.setPassword("newPass");
        user.setEmail("new@example.com");

        assertEquals("newUser", user.getUsername());
        assertEquals("newPass", user.getPassword());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testSetNewReferences() {
        Rol newRole = mock(Rol.class);
        Student newStudent = mock(Student.class);
        Profesor newProfesor = mock(Profesor.class);

        user.setRole(newRole);
        user.setStudent(newStudent);
        user.setProfesor(newProfesor);

        assertEquals(newRole, user.getRole());
        assertEquals(newStudent, user.getStudent());
        assertEquals(newProfesor, user.getProfesor());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(user.toString()); // generat de Lombok
    }

    @Test
    void testEqualsAndHashCode() {
        User user2 = new User();
        user2.setId(1);
        user2.setUsername("testUser");
        user2.setPassword("testPassword");
        user2.setEmail("test@example.com");

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }
}
