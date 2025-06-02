package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Integer> {
    List<TodoItem> findByStudent(Student student);

    // sarcini nefinalizate cu deadline egal cu o anumită dată
    List<TodoItem> findByDoneFalseAndDeadline(LocalDate date);

    // sarcini nefinalizate cu deadline între două date (de ex. în intervalul [astăzi, mâine], etc.)
    List<TodoItem> findByDoneFalseAndDeadlineBetween(LocalDate start, LocalDate end);
}
