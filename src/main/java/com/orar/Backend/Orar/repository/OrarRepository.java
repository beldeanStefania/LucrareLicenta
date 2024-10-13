package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Ora;
import com.orar.Backend.Orar.model.Orar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OrarRepository extends JpaRepository<Orar, Integer> {
    Optional<Orar> findByZiua(String ziua);
    Optional<Orar> findById(Integer id);
}
