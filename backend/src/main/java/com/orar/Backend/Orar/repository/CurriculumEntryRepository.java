package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.model.CurriculumEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumEntryRepository extends JpaRepository<CurriculumEntry, Integer> {
    List<CurriculumEntry> findBySpecializareIdAndAnAndTip(Integer specializareId, Integer an, Tip tip);
    List<CurriculumEntry> findBySpecializareIdAndAn(Integer specializareId, Integer an);
}
