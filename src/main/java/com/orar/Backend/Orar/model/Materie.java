package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "materie", cascade = ALL)
    @JsonManagedReference
    private List<CatalogStudentMaterie> catalogStudentMaterie;

    @OneToMany(mappedBy = "materie", cascade = ALL)
    @JsonManagedReference
    private List<RepartizareProf> repartizareProfs;
}
