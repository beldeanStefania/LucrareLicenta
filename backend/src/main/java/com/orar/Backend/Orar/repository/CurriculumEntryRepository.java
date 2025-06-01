package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.model.CurriculumEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumEntryRepository extends JpaRepository<CurriculumEntry, Integer> {
    List<CurriculumEntry> findBySpecializareIdAndAnAndTip(Integer specializareId, Integer an, Tip tip);
    List<CurriculumEntry> findBySpecializareIdAndAn(Integer specializareId, Integer an);

    List<CurriculumEntry> findByAnAndSemestruAndTip(Integer an, Integer semestru, Tip tip);

    List<CurriculumEntry> findBySpecializareIdAndAnAndSemestru(Integer specializareId, Integer an, Integer semestru);

    @Query("SELECT ce FROM CurriculumEntry ce " +
           "WHERE ce.specializare.id = :specializareId  AND ce.materie.id = :materieId ")
    Optional<CurriculumEntry> findBySpecializareIdAndMaterieId(@Param("specializareId") Integer specializareId,
                                                               @Param("materieId") Integer materieId);
    @Query("SELECT ce FROM CurriculumEntry ce " +
            " WHERE ce.specializare.id = :specializareId " +
            "   AND ce.an > :anUltim " +
            "   AND (ce.tip = 'OPTIONALA' OR ce.tip = 'FACULTATIVA')")
    List<CurriculumEntry> findFutureOptionalsAndFacultatives(
            @Param("specializareId") Integer specializareId,
            @Param("anUltim") Integer anUltim
    );
}
