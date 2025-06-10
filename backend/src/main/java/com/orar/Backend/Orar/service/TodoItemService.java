package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.TodoDTO;
import com.orar.Backend.Orar.exception.TodoItemNotFoundException;
import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.TodoItemRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creează un nou To-Do și îl salvează în BD
     */
    public TodoItem createTodo(String username, String title, String description, LocalDate deadline) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Userul cu username " + username + " nu există"));

        TodoItem todo = TodoItem.builder()
                .title(title)
                .description(description)
                .deadline(deadline)
                .done(false)
                .user(user)
                .build();

        return todoItemRepository.save(todo);
    }

    /**
     * Returnează lista de To-Do-uri pentru un user, mapate în DTO (inclusiv id-ul)
     */
    public List<TodoDTO> getTodosForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return todoItemRepository.findByUser(user).stream()
                .map(todo -> TodoDTO.builder()
                        .id(todo.getId())
                        .username(user.getUsername())
                        .title(todo.getTitle())
                        .description(todo.getDescription())
                        .deadline(todo.getDeadline().toString())
                        .done(todo.getDone())
                        .build()
                )
                .toList();
    }

    /**
     * Actualizează un To-Do existent
     */
    public TodoItem updateTodo(Integer todoId,
                               String newTitle,
                               String newDescription,
                               LocalDate newDeadline,
                               Boolean isDone) {
        TodoItem todo = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new TodoItemNotFoundException("TodoItem cu id " + todoId + " nu există"));

        if (newTitle != null)       todo.setTitle(newTitle);
        if (newDescription != null) todo.setDescription(newDescription);
        if (newDeadline != null)    todo.setDeadline(newDeadline);
        if (isDone != null)         todo.setDone(isDone);

        return todoItemRepository.save(todo);
    }

    /**
     * Șterge un To-Do după id
     */
    public void deleteTodo(Integer todoId) {
        if (!todoItemRepository.existsById(todoId)) {
            throw new TodoItemNotFoundException("TodoItem cu id " + todoId + " nu există");
        }
        todoItemRepository.deleteById(todoId);
    }

    public List<TodoItem> getTodosWithDeadline(LocalDate date) {
        return todoItemRepository.findByDoneFalseAndDeadline(date);
    }
}
