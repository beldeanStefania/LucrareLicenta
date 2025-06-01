package com.orar.Backend.Orar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class MateriiOptionale {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;

    @OneToMany(mappedBy = "optionale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurriculumEntry> curriculumEntries;
}
