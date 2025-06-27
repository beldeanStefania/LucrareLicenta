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
    void testSettersWithNullValues() {
        todoItem.setId(null);
        todoItem.setTitle(null);
        todoItem.setDescription(null);
        todoItem.setDeadline(null);
        todoItem.setDone(null);
        todoItem.setUser(null);

        assertNull(todoItem.getId());
        assertNull(todoItem.getTitle());
        assertNull(todoItem.getDescription());
        assertNull(todoItem.getDeadline());
        assertNull(todoItem.getDone());
        assertNull(todoItem.getUser());
    }

    @Test
    void testSettersWithEmptyStrings() {
        todoItem.setTitle("");
        todoItem.setDescription("");

        assertEquals("", todoItem.getTitle());
        assertEquals("", todoItem.getDescription());
    }

    @Test
    void testSetDoneWithBooleanValues() {
        todoItem.setDone(true);
        assertTrue(todoItem.getDone());

        todoItem.setDone(false);
        assertFalse(todoItem.getDone());

        todoItem.setDone(null);
        assertNull(todoItem.getDone());
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
    void testNoArgsConstructorDoneFieldInitialization() {
        TodoItem item = new TodoItem();
        assertEquals(false, item.getDone()); // done = false este default în entitate
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
    void testAllArgsConstructorWithNullValues() {
        TodoItem item = new TodoItem(null, null, null, null, null, null);

        assertNull(item.getId());
        assertNull(item.getTitle());
        assertNull(item.getDescription());
        assertNull(item.getDeadline());
        assertNull(item.getDone());
        assertNull(item.getUser());
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
    void testBuilderWithPartialValues() {
        TodoItem item = TodoItem.builder()
                .id(10)
                .title("Partial task")
                .build();

        assertEquals(10, item.getId());
        assertEquals("Partial task", item.getTitle());
        assertNull(item.getDescription());
        assertNull(item.getDeadline());
        assertNull(item.getDone());
        assertNull(item.getUser());
    }

    @Test
    void testBuilderWithNoValues() {
        TodoItem item = TodoItem.builder().build();

        assertNull(item.getId());
        assertNull(item.getTitle());
        assertNull(item.getDescription());
        assertNull(item.getDeadline());
        assertNull(item.getDone());
        assertNull(item.getUser());
    }

    @Test
    void testToStringIsNotNull() {
        assertNotNull(todoItem.toString()); // generat de Lombok dacă adaugi @ToString
    }

    @Test
    void testToStringWithNullFields() {
        TodoItem item = new TodoItem();
        assertNotNull(item.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        TodoItem item1 = TodoItem.builder().id(5).build();
        TodoItem item2 = TodoItem.builder().id(5).build();

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(todoItem, todoItem);
        assertTrue(todoItem.equals(todoItem));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(todoItem, null);
        assertFalse(todoItem.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a todo item";
        assertNotEquals(todoItem, differentObject);
        assertFalse(todoItem.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        TodoItem item1 = TodoItem.builder().id(1).build();
        TodoItem item2 = TodoItem.builder().id(2).build();

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        TodoItem item1 = TodoItem.builder()
                .id(1)
                .title("Task 1")
                .description("Description 1")
                .build();

        TodoItem item2 = TodoItem.builder()
                .id(1)
                .title("Task 2")
                .description("Description 2")
                .build();

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) only considers ID
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        TodoItem item1 = TodoItem.builder().id(null).build();
        TodoItem item2 = TodoItem.builder().id(null).build();

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        TodoItem item1 = TodoItem.builder().id(1).build();
        TodoItem item2 = TodoItem.builder().id(null).build();

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = todoItem.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, todoItem.hashCode());
        assertEquals(initialHashCode, todoItem.hashCode());

        // Changing non-ID fields should not affect hash code (due to @EqualsAndHashCode configuration)
        todoItem.setTitle("changedTitle");
        todoItem.setDescription("changedDescription");
        assertEquals(initialHashCode, todoItem.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = todoItem.hashCode();

        todoItem.setId(999);
        int newHashCode = todoItem.hashCode();

        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        TodoItem item = TodoItem.builder().id(null).build();
        int hashCode = item.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, item.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        TodoItem item1 = new TodoItem();
        TodoItem item2 = new TodoItem();

        assertTrue(item1.canEqual(item2));
        assertTrue(item2.canEqual(item1));

        String notTodoItem = "not a todo item";
        assertFalse(item1.canEqual(notTodoItem));
    }

    @Test
    void testFieldsWithEdgeValues() {
        TodoItem item = new TodoItem();

        // Test with very long strings
        String longTitle = "a".repeat(1000);
        String longDescription = "b".repeat(2000);
        item.setTitle(longTitle);
        item.setDescription(longDescription);

        assertEquals(longTitle, item.getTitle());
        assertEquals(longDescription, item.getDescription());

        // Test with special characters
        item.setTitle("Special chars: åäö!@#$%^&*()");
        item.setDescription("Unicode: 😀🚀💻");

        assertEquals("Special chars: åäö!@#$%^&*()", item.getTitle());
        assertEquals("Unicode: 😀🚀💻", item.getDescription());
    }

    @Test
    void testDateFieldEdgeCases() {
        TodoItem item = new TodoItem();

        // Test with past date
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        item.setDeadline(pastDate);
        assertEquals(pastDate, item.getDeadline());

        // Test with future date
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        item.setDeadline(futureDate);
        assertEquals(futureDate, item.getDeadline());

        // Test with current date
        LocalDate currentDate = LocalDate.now();
        item.setDeadline(currentDate);
        assertEquals(currentDate, item.getDeadline());
    }

    @Test
    void testCompleteObjectCreationAndModification() {
        User anotherUser = mock(User.class);

        TodoItem item = TodoItem.builder()
                .id(100)
                .title("Complete Task")
                .description("Full description")
                .deadline(LocalDate.of(2025, 12, 25))
                .done(false)
                .user(user)
                .build();

        // Verify initial state
        assertEquals(100, item.getId());
        assertEquals("Complete Task", item.getTitle());
        assertEquals("Full description", item.getDescription());
        assertEquals(LocalDate.of(2025, 12, 25), item.getDeadline());
        assertFalse(item.getDone());
        assertEquals(user, item.getUser());

        // Modify all fields
        item.setId(200);
        item.setTitle("Modified Task");
        item.setDescription("Modified description");
        item.setDeadline(LocalDate.of(2026, 1, 1));
        item.setDone(true);
        item.setUser(anotherUser);

        // Verify modified state
        assertEquals(200, item.getId());
        assertEquals("Modified Task", item.getTitle());
        assertEquals("Modified description", item.getDescription());
        assertEquals(LocalDate.of(2026, 1, 1), item.getDeadline());
        assertTrue(item.getDone());
        assertEquals(anotherUser, item.getUser());
    }
}
