package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
