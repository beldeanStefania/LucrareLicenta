package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.TodoDTO;
import com.orar.Backend.Orar.exception.TodoItemNotFoundException;
import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TodoItemControllerTest {

    @Mock
    private TodoItemService todoItemService;

    @InjectMocks
    private TodoItemController todoItemController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoItemController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getTodosForUser_Success() throws Exception {
        // Given
        String username = "testuser";
        List<TodoDTO> expectedTodos = Arrays.asList(
                TodoDTO.builder()
                        .id(1)
                        .username(username)
                        .title("Test Todo 1")
                        .description("Description 1")
                        .deadline("2025-12-31")
                        .done(false)
                        .build(),
                TodoDTO.builder()
                        .id(2)
                        .username(username)
                        .title("Test Todo 2")
                        .description("Description 2")
                        .deadline("2025-11-30")
                        .done(true)
                        .build()
        );

        when(todoItemService.getTodosForUser(username)).thenReturn(expectedTodos);

        // When & Then
        mockMvc.perform(get("/api/todo/user/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value(username))
                .andExpect(jsonPath("$[0].title").value("Test Todo 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].deadline").value("2025-12-31"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].done").value(true));

        verify(todoItemService, times(1)).getTodosForUser(username);
    }

    @Test
    void getTodosForUser_EmptyList() throws Exception {
        // Given
        String username = "emptyuser";
        when(todoItemService.getTodosForUser(username)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/todo/user/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(todoItemService, times(1)).getTodosForUser(username);
    }

    @Test
    void createTodo_Success() throws Exception {
        // Given
        TodoDTO todoDTO = TodoDTO.builder()
                .username("testuser")
                .title("New Todo")
                .description("New Description")
                .deadline("2025-12-31")
                .build();

        User user = new User();
        user.setUsername("testuser");
        TodoItem savedTodo = TodoItem.builder()
                .id(1)
                .title("New Todo")
                .description("New Description")
                .deadline(LocalDate.parse("2025-12-31"))
                .done(false)
                .user(user)
                .build();

        when(todoItemService.createTodo(
                eq("testuser"),
                eq("New Todo"),
                eq("New Description"),
                eq(LocalDate.parse("2025-12-31"))
        )).thenReturn(savedTodo);

        // When & Then
        mockMvc.perform(post("/api/todo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.done").value(false));

        verify(todoItemService, times(1)).createTodo(
                eq("testuser"),
                eq("New Todo"),
                eq("New Description"),
                eq(LocalDate.parse("2025-12-31"))
        );
    }

    @Test
    void createTodo_MissingDeadline_BadRequest() throws Exception {
        // Given
        TodoDTO todoDTO = TodoDTO.builder()
                .username("testuser")
                .title("New Todo")
                .description("New Description")
                .deadline("") // deadline gol
                .build();

        // When & Then
        mockMvc.perform(post("/api/todo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Câmpul 'deadline' este obligatoriu.\"}"));

        verify(todoItemService, never()).createTodo(anyString(), anyString(), anyString(), any(LocalDate.class));
    }

    @Test
    void createTodo_NullDeadline_BadRequest() throws Exception {
        // Given
        TodoDTO todoDTO = TodoDTO.builder()
                .username("testuser")
                .title("New Todo")
                .description("New Description")
                .deadline(null) // deadline null
                .build();

        // When & Then
        mockMvc.perform(post("/api/todo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Câmpul 'deadline' este obligatoriu.\"}"));

        verify(todoItemService, never()).createTodo(anyString(), anyString(), anyString(), any(LocalDate.class));
    }

    @Test
    void createTodo_ServiceThrowsRuntimeException_NotFound() throws Exception {
        // Given
        TodoDTO todoDTO = TodoDTO.builder()
                .username("nonexistentuser")
                .title("New Todo")
                .description("New Description")
                .deadline("2025-12-31")
                .build();

        when(todoItemService.createTodo(anyString(), anyString(), anyString(), any(LocalDate.class)))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        mockMvc.perform(post("/api/todo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"User not found\"}"));
    }

    @Test
    void updateTodo_Success() throws Exception {
        // Given
        Integer todoId = 1;
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .deadline("2025-12-31")
                .done(true)
                .build();

        User user = new User();
        user.setUsername("testuser");
        TodoItem updatedTodo = TodoItem.builder()
                .id(todoId)
                .title("Updated Todo")
                .description("Updated Description")
                .deadline(LocalDate.parse("2025-12-31"))
                .done(true)
                .user(user)
                .build();

        when(todoItemService.updateTodo(
                eq(todoId),
                eq("Updated Todo"),
                eq("Updated Description"),
                eq(LocalDate.parse("2025-12-31")),
                eq(true)
        )).thenReturn(updatedTodo);

        // When & Then
        mockMvc.perform(put("/api/todo/update/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.done").value(true));

        verify(todoItemService, times(1)).updateTodo(
                eq(todoId),
                eq("Updated Todo"),
                eq("Updated Description"),
                eq(LocalDate.parse("2025-12-31")),
                eq(true)
        );
    }

    @Test
    void updateTodo_WithoutDeadline_Success() throws Exception {
        // Given
        Integer todoId = 1;
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .deadline(null) // nu specificăm deadline
                .done(true)
                .build();

        User user = new User();
        user.setUsername("testuser");
        TodoItem updatedTodo = TodoItem.builder()
                .id(todoId)
                .title("Updated Todo")
                .description("Updated Description")
                .deadline(LocalDate.parse("2025-01-01")) // deadline vechi
                .done(true)
                .user(user)
                .build();

        when(todoItemService.updateTodo(
                eq(todoId),
                eq("Updated Todo"),
                eq("Updated Description"),
                isNull(),
                eq(true)
        )).thenReturn(updatedTodo);

        // When & Then
        mockMvc.perform(put("/api/todo/update/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.done").value(true));

        verify(todoItemService, times(1)).updateTodo(
                eq(todoId),
                eq("Updated Todo"),
                eq("Updated Description"),
                isNull(),
                eq(true)
        );
    }

    @Test
    void updateTodo_InvalidDateFormat_BadRequest() throws Exception {
        // Given
        Integer todoId = 1;
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .deadline("invalid-date")
                .done(true)
                .build();

        // When & Then
        mockMvc.perform(put("/api/todo/update/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Format invalid al câmpului 'deadline'. Trebuie YYYY-MM-DD.\"}"));

        verify(todoItemService, never()).updateTodo(anyInt(), anyString(), anyString(), any(), any());
    }

    @Test
    void updateTodo_TodoNotFound_NotFound() throws Exception {
        // Given
        Integer todoId = 999;
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .deadline("2025-12-31")
                .done(true)
                .build();

        when(todoItemService.updateTodo(anyInt(), anyString(), anyString(), any(LocalDate.class), any(Boolean.class)))
                .thenThrow(new TodoItemNotFoundException("Todo item not found"));

        // When & Then
        mockMvc.perform(put("/api/todo/update/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Todo item not found\"}"));
    }

    @Test
    void updateTodo_ServiceThrowsException_InternalServerError() throws Exception {
        // Given
        Integer todoId = 1;
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .deadline("2025-12-31")
                .done(true)
                .build();

        when(todoItemService.updateTodo(anyInt(), anyString(), anyString(), any(LocalDate.class), any(Boolean.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(put("/api/todo/update/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Eroare intern")))
                .andExpect(content().string(containsString("Database error")));
    }

    @Test
    void deleteTodo_Success() throws Exception {
        // Given
        Integer todoId = 1;
        doNothing().when(todoItemService).deleteTodo(todoId);

        // When & Then
        mockMvc.perform(delete("/api/todo/delete/{todoId}", todoId))
                .andExpect(status().isOk());

        verify(todoItemService, times(1)).deleteTodo(todoId);
    }

    @Test
    void deleteTodo_TodoNotFound_NotFound() throws Exception {
        // Given
        Integer todoId = 999;
        doThrow(new TodoItemNotFoundException("Todo item not found"))
                .when(todoItemService).deleteTodo(todoId);

        // When & Then
        mockMvc.perform(delete("/api/todo/delete/{todoId}", todoId))
                .andExpect(status().isNotFound());

        verify(todoItemService, times(1)).deleteTodo(todoId);
    }

    @Test
    void deleteTodo_ServiceThrowsException_InternalServerError() throws Exception {
        // Given
        Integer todoId = 1;
        doThrow(new RuntimeException("Database error"))
                .when(todoItemService).deleteTodo(todoId);

        // When & Then
        mockMvc.perform(delete("/api/todo/delete/{todoId}", todoId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Eroare intern")))
                .andExpect(content().string(containsString("Database error")));

        verify(todoItemService, times(1)).deleteTodo(todoId);
    }
}