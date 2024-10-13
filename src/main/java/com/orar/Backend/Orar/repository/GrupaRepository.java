package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupaRepository extends JpaRepository<Grupa, Integer> {
    Optional<Grupa> findById(Integer id);

    Optional<Grupa> findByNume(String nume);
}
