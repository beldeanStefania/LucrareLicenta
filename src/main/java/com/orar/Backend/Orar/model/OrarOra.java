package com.orar.Backend.Orar.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class OrarOra {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "orar_id")
    private Orar orar;

    @ManyToOne
    @JoinColumn(name = "ora_id")
    private Ora ora;
}
