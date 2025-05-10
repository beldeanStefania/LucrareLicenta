package com.orar.Backend.Orar.repository;

import com.orar.Backend.Orar.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByStudentCodAndAnContract(String studentCod, int anContract);
    boolean existsByStudentCodAndAnContract(String studentCod, int anContract);
}
