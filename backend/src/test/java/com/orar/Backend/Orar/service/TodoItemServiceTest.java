package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.TodoDTO;
import com.orar.Backend.Orar.exception.TodoItemNotFoundException;
import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.TodoItemRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    private User user;
    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");

        todoItem = TodoItem.builder()
                .id(1)
                .title("Test")
                .description("Description")
                .deadline(LocalDate.now())
                .done(false)
                .user(user)
                .build();
    }

    @Test
    void testCreateTodo() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        TodoItem result = todoItemService.createTodo("testuser", "Test", "Description", LocalDate.now());
        assertEquals("Test", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertFalse(result.getDone());
    }

    @Test
    void testCreateTodo_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> todoItemService.createTodo("testuser", "a", "b", LocalDate.now()));
    }

    @Test
    void testGetTodosForUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(todoItemRepository.findByUser(user)).thenReturn(List.of(todoItem));

        List<TodoDTO> dtos = todoItemService.getTodosForUser("testuser");
        assertEquals(1, dtos.size());
        assertEquals("Test", dtos.get(0).getTitle());
    }

    @Test
    void testGetTodosForUser_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> todoItemService.getTodosForUser("testuser"));
    }

    @Test
    void testUpdateTodo() {
        when(todoItemRepository.findById(1)).thenReturn(Optional.of(todoItem));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        TodoItem updated = todoItemService.updateTodo(1, "New Title", "New Desc", LocalDate.now(), true);
        assertEquals("New Title", updated.getTitle());
        assertTrue(updated.getDone());
    }

    @Test
    void testUpdateTodo_NotFound() {
        when(todoItemRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(TodoItemNotFoundException.class, () ->
                todoItemService.updateTodo(1, "title", "desc", LocalDate.now(), true));
    }

    @Test
    void testDeleteTodo_Success() {
        when(todoItemRepository.existsById(1)).thenReturn(true);
        doNothing().when(todoItemRepository).deleteById(1);

        assertDoesNotThrow(() -> todoItemService.deleteTodo(1));
        verify(todoItemRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteTodo_NotFound() {
        when(todoItemRepository.existsById(1)).thenReturn(false);
        assertThrows(TodoItemNotFoundException.class, () -> todoItemService.deleteTodo(1));
    }

    @Test
    void testGetTodosWithDeadline() {
        LocalDate date = LocalDate.now();
        when(todoItemRepository.findByDoneFalseAndDeadline(date)).thenReturn(List.of(todoItem));

        List<TodoItem> items = todoItemService.getTodosWithDeadline(date);
        assertEquals(1, items.size());
    }
}
