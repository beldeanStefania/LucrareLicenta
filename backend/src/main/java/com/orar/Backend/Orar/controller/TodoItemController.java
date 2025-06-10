package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.TodoDTO;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.exception.TodoItemNotFoundException;
import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.service.TodoItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoItemController {

    @Autowired
    private final TodoItemService todoItemService;

    /**
     * GET /api/todo/student/{cod}
     * Returnează toate to-do-urile pentru studentul cu codul specificat.
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<TodoDTO>> getTodosForUser(@PathVariable String username) {
        List<TodoDTO> dtos = todoItemService.getTodosForUser(username);
        return ResponseEntity.ok(dtos);
    }

    /**
     * POST /api/todo/create
     * Așteaptă în body JSON:
     * { "studentCod": "...", "title": "...", "description": "...", "deadline": "YYYY-MM-DD" }
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO) {
        try {
            if (todoDTO.getDeadline() == null || todoDTO.getDeadline().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("{\"error\":\"Câmpul 'deadline' este obligatoriu.\"}");
            }
            LocalDate dl = LocalDate.parse(todoDTO.getDeadline());

            TodoItem saved = todoItemService.createTodo(
                    todoDTO.getUsername(),
                    todoDTO.getTitle(),
                    todoDTO.getDescription(),
                    dl
            );
            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\":\"Eroare internă: " + e.getMessage() + "\"}");
        }
    }

    /**
     * PUT /api/todo/update/{todoId}
     * Poate actualiza title, description, deadline și done.
     * Exemplu body: { "title": "...", "description":"...", "deadline":"2025-06-20", "done":true }
     */
    @PutMapping("/update/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable Integer todoId, @RequestBody TodoDTO todoDTO) {
        try {
            // Dacă vine deadline, îl parseăm
            LocalDate newDl = null;
            if (todoDTO.getDeadline() != null && !todoDTO.getDeadline().isBlank()) {
                try {
                    newDl = LocalDate.parse(todoDTO.getDeadline());
                } catch (Exception ex) {
                    return ResponseEntity.badRequest()
                            .body("{\"error\":\"Format invalid al câmpului 'deadline'. Trebuie YYYY-MM-DD.\"}");
                }
            }

            TodoItem updated = todoItemService.updateTodo(
                    todoId,
                    todoDTO.getTitle(),
                    todoDTO.getDescription(),
                    newDl,
                    todoDTO.getDone()
            );
            return ResponseEntity.ok(updated);

        } catch (TodoItemNotFoundException ex) {
            return ResponseEntity.status(404)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body("{\"error\":\"Eroare internă: " + ex.getMessage() + "\"}");
        }
    }

    /**
     * DELETE /api/todo/delete/{todoId}
     */
    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Integer todoId) {
        try {
            todoItemService.deleteTodo(todoId);
            return ResponseEntity.ok().build();
        } catch (TodoItemNotFoundException ex) {
            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body("{\"error\":\"Eroare internă: " + ex.getMessage() + "\"}");
        }
    }
}
