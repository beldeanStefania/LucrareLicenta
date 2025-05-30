package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.orar.Backend.Orar.enums.MaterieStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class CatalogStudentMaterie {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Double nota;
    private Integer semestru;

    @Enumerated(EnumType.STRING)
    private MaterieStatus status;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "materie_id", nullable = false)
    private Materie materie;
}
