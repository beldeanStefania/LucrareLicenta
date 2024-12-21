package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Orar {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    @JsonBackReference
    private Sala sala;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "repartizareProf_id", nullable = false)
    @JsonBackReference
    private RepartizareProf repartizareProf;


    private int oraInceput;
    private int oraSfarsit;
    private String grupa;
    private String zi;
}
