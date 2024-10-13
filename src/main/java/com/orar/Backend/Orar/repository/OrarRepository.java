package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Orar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrarRepository extends JpaRepository<Orar, Integer> {

    Optional<Object> findByGrupaAndZi(String grupa, String ziua);
    Optional<Object> findByGrupaAndZiAndOraInceputAndOraSfarsit(String grupa, String ziua, Integer oraInceput, Integer oraSfarsit);
}
