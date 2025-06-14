package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoItemTest {

    private TodoItem todoItem;
    private User user;

    @BeforeEach
    void setUp() {
        user = mock(User.class);

        todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setTitle("Study JPA");
        todoItem.setDescription("Learn @ManyToOne mapping");
        todoItem.setDeadline(LocalDate.of(2025, 6, 30));
        todoItem.setDone(true);
        todoItem.setUser(user);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, todoItem.getId());
        assertEquals("Study JPA", todoItem.getTitle());
        assertEquals("Learn @ManyToOne mapping", todoItem.getDescription());
        assertEquals(LocalDate.of(2025, 6, 30), todoItem.getDeadline());
        assertTrue(todoItem.getDone());
        assertEquals(user, todoItem.getUser());
    }

    @Test
    void testNoArgsConstructorDefaults() {
        TodoItem empty = new TodoItem();
        assertNull(empty.getId());
        assertNull(empty.getTitle());
        assertNull(empty.getDescription());
        assertNull(empty.getDeadline());
        assertNull(empty.getUser());
        assertFalse(empty.getDone() != null && empty.getDone()); // done e null dacă nu-l setezi
    }

    @Test
    void testAllArgsConstructor() {
        TodoItem item = new TodoItem(
                2,
                "Write tests",
                "Cover entity classes",
                LocalDate.of(2025, 7, 1),
                true,
                user
        );

        assertEquals(2, item.getId());
        assertEquals("Write tests", item.getTitle());
        assertEquals("Cover entity classes", item.getDescription());
        assertEquals(LocalDate.of(2025, 7, 1), item.getDeadline());
        assertTrue(item.getDone());
        assertEquals(user, item.getUser());
    }

    @Test
    void testBuilder() {
        TodoItem item = TodoItem.builder()
                .id(3)
                .title("Do gym")
                .description("Chest day")
                .deadline(LocalDate.of(2025, 7, 5))
                .done(false)
                .user(user)
                .build();

        assertEquals(3, item.getId());
        assertEquals("Do gym", item.getTitle());
        assertEquals("Chest day", item.getDescription());
        assertEquals(LocalDate.of(2025, 7, 5), item.getDeadline());
        assertFalse(item.getDone());
        assertEquals(user, item.getUser());
    }

    @Test
    void testToStringIsNotNull() {
        assertNotNull(todoItem.toString()); // generat de Lombok dacă adaugi @ToString
    }

    @Test
    void testEqualsAndHashCode() {
        TodoItem item1 = TodoItem.builder().id(5).build();
        TodoItem item2 = TodoItem.builder().id(5).build();

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}
