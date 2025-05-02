package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.repository.SpecializareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializareService {

    @Autowired
    private SpecializareRepository specializareRepository;

    public List<Specializare> getAll() {
        return specializareRepository.findAll();
    }
}
