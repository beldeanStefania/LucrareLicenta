package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.GrupaDTO;
import com.orar.Backend.Orar.model.Grupa;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.GrupaRepository;
import com.orar.Backend.Orar.repository.OrarRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupaService {

    @Autowired
    private GrupaRepository grupaRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OrarRepository orarRepository;

    public List<Grupa> getAll() {
        return grupaRepository.findAll();
    }

    public Grupa add(GrupaDTO grupaDTO) {
        Grupa grupa = new Grupa();
        grupa.setNume(grupaDTO.getNume());
        return grupaRepository.save(grupa);
    }

    public void delete(int id) {
        grupaRepository.deleteById(id);
    }
}
