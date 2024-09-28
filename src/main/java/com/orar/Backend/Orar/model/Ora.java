package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Ora {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer Id;

    @Embedded
    private TipOra tip;

    @ManyToOne
    @JoinColumn(name = "orar_id", nullable = false)
    @JsonBackReference
    private Orar orar;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    @JsonBackReference
    private Sala sala;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonBackReference
    private Profesor profesor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "materie_id")
    @JsonBackReference
    private Materie materie;

//    @ManyToMany
//    @JoinTable(
//            name = "ora_profesor",
//            joinColumns = @JoinColumn(name = "ora_id"),
//            inverseJoinColumns = @JoinColumn(name = "profesor_id")
//    )
//    private List<Profesor> profesori = new ArrayList<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "materie_ora",
//            joinColumns = @JoinColumn(name = "ora_id"),
//            inverseJoinColumns = @JoinColumn(name = "materie_id")
//    )
//    private List<Materie> materii = new ArrayList<>();
}
