package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Materie {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private Integer semestru;
    private Integer an;

    @Column(unique = true)
    private String cod;
    private Integer credite;

    @OneToMany(mappedBy = "materie", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CatalogStudentMaterie> catalogStudentMaterie;

    @OneToMany(mappedBy = "materie", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RepartizareProf> repartizareProfs;
}
