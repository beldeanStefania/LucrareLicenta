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

    private String cod;
    private String nume;
    private String prenume;
    private Integer an;
    private String grupa;


    @OneToMany(mappedBy = "student", cascade = ALL)
    @JsonManagedReference
    private List<CatalogStudentMaterie> catalogStudentMaterie;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
