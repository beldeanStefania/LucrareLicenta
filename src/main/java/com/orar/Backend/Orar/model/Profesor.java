package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Profesor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer Id;

    private String nume;
    private String prenume;

    @ManyToOne
    @JoinColumn(name = "ora_id", nullable = false)
    @JsonBackReference
    private Ora ora;
}
