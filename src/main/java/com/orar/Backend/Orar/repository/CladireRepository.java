package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Cladire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CladireRepository extends JpaRepository<Cladire, Integer> {
    Optional<Cladire> findByNume(String nume);
}
