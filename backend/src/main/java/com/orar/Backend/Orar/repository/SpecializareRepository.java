package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Specializare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializareRepository extends JpaRepository<Specializare, Integer> {
    Specializare findBySpecializare(String nume);
}
