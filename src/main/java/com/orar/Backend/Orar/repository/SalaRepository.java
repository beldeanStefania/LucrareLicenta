package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Integer> {
    Optional<Sala> findByNume(String name);
}
