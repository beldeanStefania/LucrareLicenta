package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Ora;
import com.orar.Backend.Orar.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    Optional<Profesor> findByNumeAndPrenume(String nume, String prenume);
}
