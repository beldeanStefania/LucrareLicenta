package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.CurriculumEntry;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.model.Specializare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrarRepository extends JpaRepository<Orar, Integer> {

    List<Orar> findByGrupa(String grupa);

    @Query("SELECT o FROM Orar o WHERE o.sala.id = :salaId AND o.zi = :zi AND "
            + "(o.oraInceput < :oraSfarsit AND o.oraSfarsit > :oraInceput)")
    List<Orar> findOverlappingOrar(@Param("salaId") Integer salaId,
                                   @Param("zi") String zi,
                                   @Param("oraInceput") int oraInceput,
                                   @Param("oraSfarsit") int oraSfarsit);

    List<Orar> findByRepartizareProf_Profesor_Id(Integer profesorId);

    @Query("SELECT c FROM CurriculumEntry c WHERE c.specializare = :spec")
    List<CurriculumEntry> findCurriculumBySpecializare(Specializare spec);
}