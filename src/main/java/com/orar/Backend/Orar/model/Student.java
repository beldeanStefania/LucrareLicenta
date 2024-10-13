package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private String prenume;
    private Integer an;

    @ManyToOne
    @JoinColumn(name = "grupa_id", nullable = false)
    @JsonBackReference
    private Grupa grupa;

    @OneToMany(mappedBy = "student", cascade = ALL)
    @JsonManagedReference
    private List<CatalogStudentMaterie> catalogStudentMaterie;
}
