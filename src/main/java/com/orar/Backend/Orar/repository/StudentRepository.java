package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByNumeAndPrenume(String nume, String prenume);
}
