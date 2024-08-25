package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Sala {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private Integer capacitate;

    @ManyToOne
    @JoinColumn(name = "cladire_id", nullable = false)
    @JsonBackReference
    private Cladire cladire;
}
