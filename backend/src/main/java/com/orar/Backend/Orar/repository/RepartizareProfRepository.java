package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.RepartizareProf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepartizareProfRepository extends JpaRepository<RepartizareProf, Integer> {
    Optional<RepartizareProf> findByProfesorAndMaterieAndTip(Profesor profesor, Materie materie, String tip);
}
