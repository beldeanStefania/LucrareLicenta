package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Materie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterieRepository extends JpaRepository<Materie, Integer> {

    Optional<Materie> findByNume(String nume);
    Optional<Materie> findByCod(String cod);
    Optional<Materie> findByNumeAndCod(String nume, String cod);
    List<Materie> findAllByCodIn(List<String> coduri);
}
