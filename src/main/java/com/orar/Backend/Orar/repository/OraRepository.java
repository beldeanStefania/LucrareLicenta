package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OraRepository extends JpaRepository<Ora, Integer> {

    Optional<Ora> findById(Integer id);
    Optional<Ora> findByOrarAndProfesorAndMaterieAndSalaAndOraInceputAndOraSfarsit(Orar orar, Profesor profesor, Materie materie, Sala sala, LocalTime oraInceput, LocalTime oraSfarsit);
}
