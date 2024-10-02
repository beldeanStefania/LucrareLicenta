package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OraRepository extends JpaRepository<Ora, Integer> {
    Optional<Ora> findByOrar_IdAndSala_IdAndProfesor_IdAndMaterie_IdAndTip(
            Integer orarId, Integer salaId, Integer profesorId, Integer materieId, TipOra tip);
    List<Ora> findBySala_IdAndOrar_Ziua(Integer salaId, String ziua);

    List<Ora> findByProfesor_IdAndOrar_Ziua(Integer profesorId, String ziua);

    List<Ora> findByOrar_Id(Integer orarId);
}
