package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogStudentMaterieRepository extends JpaRepository<CatalogStudentMaterie, Integer> {
    Optional<CatalogStudentMaterie> findByStudentNumeAndStudentPrenumeAndMaterieId(String nume, String prenume, Integer materieId);
    Optional<CatalogStudentMaterie> findByStudentIdAndMaterieId(Integer studentId, Integer materieId);
}
