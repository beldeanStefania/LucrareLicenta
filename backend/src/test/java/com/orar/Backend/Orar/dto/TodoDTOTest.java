package com.orar.Backend.Orar.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        TodoDTO dto = new TodoDTO();
        dto.setId(1);
        dto.setUsername("simon");
        dto.setTitle("Learn Spring Boot");
        dto.setDescription("Finish the tutorial");
        dto.setDeadline("2025-06-15");
        dto.setDone(true);

        assertEquals(1, dto.getId());
        assertEquals("simon", dto.getUsername());
        assertEquals("Learn Spring Boot", dto.getTitle());
        assertEquals("Finish the tutorial", dto.getDescription());
        assertEquals("2025-06-15", dto.getDeadline());
        assertTrue(dto.getDone());
    }

    @Test
    void testAllArgsConstructor() {
        TodoDTO dto = new TodoDTO(2, "john", "Clean code", "Refactor services", "2025-06-20", false);

        assertEquals(2, dto.getId());
        assertEquals("john", dto.getUsername());
        assertEquals("Clean code", dto.getTitle());
        assertEquals("Refactor services", dto.getDescription());
        assertEquals("2025-06-20", dto.getDeadline());
        assertFalse(dto.getDone());
    }

    @Test
    void testBuilder() {
        TodoDTO dto = TodoDTO.builder()
                .id(3)
                .username("alex")
                .title("Write tests")
                .description("Cover DTO classes")
                .deadline("2025-06-30")
                .done(true)
                .build();

        assertEquals(3, dto.getId());
        assertEquals("alex", dto.getUsername());
        assertEquals("Write tests", dto.getTitle());
        assertEquals("Cover DTO classes", dto.getDescription());
        assertEquals("2025-06-30", dto.getDeadline());
        assertTrue(dto.getDone());
    }

    @Test
    void testToStringIsNotNull() {
        TodoDTO dto = new TodoDTO();
        assertNotNull(dto.toString());
    }
}
