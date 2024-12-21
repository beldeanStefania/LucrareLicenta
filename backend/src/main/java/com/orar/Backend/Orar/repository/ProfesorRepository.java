package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    Optional<Profesor> findByNumeAndPrenume(String nume, String prenume);

    Optional<Profesor> findByNume(String profesor);
}
