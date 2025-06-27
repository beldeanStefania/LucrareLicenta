package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void testSettersWithNullValues() {
        user.setUsername(null);
        user.setPassword(null);
        user.setEmail(null);
        user.setRole(null);
        user.setStudent(null);
        user.setProfesor(null);

        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getRole());
        assertNull(user.getStudent());
        assertNull(user.getProfesor());
    }

    @Test
    void testSetIdSpecifically() {
        user.setId(999);
        assertEquals(999, user.getId());

        user.setId(0);
        assertEquals(0, user.getId());

        user.setId(-1);
        assertEquals(-1, user.getId());
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
    void testSetTodoItemsList() {
        List<TodoItem> newTodoList = new ArrayList<>();
        newTodoList.add(todo1);
        newTodoList.add(todo2);

        user.setTodoItems(newTodoList);

        assertEquals(2, user.getTodoItems().size());
        assertTrue(user.getTodoItems().contains(todo1));
        assertTrue(user.getTodoItems().contains(todo2));
        assertSame(newTodoList, user.getTodoItems());
    }

    @Test
    void testSetTodoItemsWithNull() {
        user.setTodoItems(null);
        assertNull(user.getTodoItems());
    }

    @Test
    void testTodoItemsInitialization() {
        User newUser = new User();
        assertNotNull(newUser.getTodoItems());
        assertTrue(newUser.getTodoItems().isEmpty());
    }

    @Test
    void testTodoItemsManipulation() {
        assertTrue(user.getTodoItems().isEmpty());

        user.getTodoItems().add(todo1);
        assertEquals(1, user.getTodoItems().size());

        user.getTodoItems().remove(todo1);
        assertTrue(user.getTodoItems().isEmpty());

        user.getTodoItems().addAll(List.of(todo1, todo2));
        assertEquals(2, user.getTodoItems().size());

        user.getTodoItems().clear();
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
    void testUpdateFieldsWithEmptyStrings() {
        user.setUsername("");
        user.setPassword("");
        user.setEmail("");

        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals("", user.getEmail());
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

    @Test
    void testEqualsSameObject() {
        assertEquals(user, user);
        assertTrue(user.equals(user));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(user, null);
        assertFalse(user.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a user";
        assertNotEquals(user, differentObject);
        assertFalse(user.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        User user2 = new User();
        user2.setId(2); // different ID
        user2.setUsername("testUser");
        user2.setPassword("testPassword");
        user2.setEmail("test@example.com");

        assertNotEquals(user, user2);
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testEqualsWithDifferentFields() {
        User user2 = new User();
        user2.setId(1); // same ID
        user2.setUsername("differentUser");
        user2.setPassword("differentPassword");
        user2.setEmail("different@example.com");

        // Should still be equal because equals only compares ID
        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = user.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, user.hashCode());
        assertEquals(initialHashCode, user.hashCode());

        // Changing non-ID fields should not affect hash code
        user.setUsername("changedUsername");
        user.setPassword("changedPassword");
        user.setEmail("changed@example.com");
        assertEquals(initialHashCode, user.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = user.hashCode();

        user.setId(999);
        int newHashCode = user.hashCode();

        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithZeroId() {
        user.setId(0);
        int hashCode = user.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, user.hashCode());
    }

    @Test
    void testEqualsWithBothUsersHavingZeroId() {
        User user1 = new User();
        user1.setId(0);

        User user2 = new User();
        user2.setId(0);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testDefaultConstructor() {
        User newUser = new User();

        assertEquals(0, newUser.getId()); // default int value
        assertNull(newUser.getUsername());
        assertNull(newUser.getPassword());
        assertNull(newUser.getEmail());
        assertNull(newUser.getRole());
        assertNull(newUser.getStudent());
        assertNull(newUser.getProfesor());
        assertNotNull(newUser.getTodoItems());
        assertTrue(newUser.getTodoItems().isEmpty());
    }

    @Test
    void testCompleteUserSetup() {
        User completeUser = new User();
        completeUser.setId(100);
        completeUser.setUsername("completeUser");
        completeUser.setPassword("securePassword");
        completeUser.setEmail("complete@example.com");

        Rol testRole = mock(Rol.class);
        Student testStudent = mock(Student.class);
        Profesor testProfesor = mock(Profesor.class);

        completeUser.setRole(testRole);
        completeUser.setStudent(testStudent);
        completeUser.setProfesor(testProfesor);

        List<TodoItem> todos = new ArrayList<>();
        todos.add(todo1);
        todos.add(todo2);
        completeUser.setTodoItems(todos);

        // Verify all fields are set correctly
        assertEquals(100, completeUser.getId());
        assertEquals("completeUser", completeUser.getUsername());
        assertEquals("securePassword", completeUser.getPassword());
        assertEquals("complete@example.com", completeUser.getEmail());
        assertEquals(testRole, completeUser.getRole());
        assertEquals(testStudent, completeUser.getStudent());
        assertEquals(testProfesor, completeUser.getProfesor());
        assertEquals(2, completeUser.getTodoItems().size());
        assertTrue(completeUser.getTodoItems().contains(todo1));
        assertTrue(completeUser.getTodoItems().contains(todo2));
    }
}
