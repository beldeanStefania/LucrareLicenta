package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogStudentMaterieRepository extends JpaRepository<CatalogStudentMaterie, Integer> {
    Optional<CatalogStudentMaterie> findByStudentCodAndMaterieCod(String studentCod, String materieCod);
    List<CatalogStudentMaterie> findByStudentCod(String studentCod);
    List<CatalogStudentMaterie> findByStudent(Student student);

}

