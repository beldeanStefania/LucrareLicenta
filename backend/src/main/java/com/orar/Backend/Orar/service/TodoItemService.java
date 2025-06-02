package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.exception.TodoItemNotFoundException;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.model.TodoItem;
import com.orar.Backend.Orar.repository.StudentRepository;
import com.orar.Backend.Orar.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private StudentRepository studentRepository;

    public TodoItem createTodo(String cod, String title, String description, LocalDate deadline)
            throws StudentNotFoundException {
        Student student = studentRepository.findByCod(cod)
                .orElseThrow(() -> new StudentNotFoundException("Student cu cod " + cod + " nu există"));

        TodoItem todo = TodoItem.builder()
                .title(title)
                .description(description)
                .deadline(deadline)
                .done(false)
                .student(student)
                .build();

        return todoItemRepository.save(todo);
    }

    public List<TodoItem> getTodosForStudentByCod(String cod) throws StudentNotFoundException {
        Student student = studentRepository.findByCod(cod)
                .orElseThrow(() -> new StudentNotFoundException("Student cu cod " + cod + " nu există"));

        return todoItemRepository.findByStudent(student);
    }

    public TodoItem updateTodo(Integer todoId, String newTitle, String newDescription,
                               LocalDate newDeadline, Boolean isDone) {
        TodoItem todo = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new TodoItemNotFoundException("TodoItem cu id " + todoId + " nu există"));

        if (newTitle != null) todo.setTitle(newTitle);
        if (newDescription != null) todo.setDescription(newDescription);
        if (newDeadline != null) todo.setDeadline(newDeadline);
        if (isDone != null) todo.setDone(isDone);

        return todoItemRepository.save(todo);
    }

    public void deleteTodo(Integer todoId) {
        if (!todoItemRepository.existsById(todoId)) {
            throw new TodoItemNotFoundException("TodoItem cu id " + todoId + " nu există");
        }
        todoItemRepository.deleteById(todoId);
    }

    public List<TodoItem> getTodosWithDeadline(LocalDate date) {
        return todoItemRepository.findByDoneFalseAndDeadline(date);
    }

    /**
     * (Opțional) dacă vrei să trimiți remindere pentru un interval mai larg:
     */
    public List<TodoItem> getTodosWithDeadlineBetween(LocalDate start, LocalDate end) {
        return todoItemRepository.findByDoneFalseAndDeadlineBetween(start, end);
    }
}
