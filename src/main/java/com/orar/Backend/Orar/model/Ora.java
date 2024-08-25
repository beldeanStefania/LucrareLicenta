package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "ora", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Profesor> profesor;

    @ManyToOne
    @JoinColumn(name = "orar_id", nullable = false)
    @JsonBackReference
    private Orar orar;

    @ManyToMany
    @JoinTable(
            name = "materie_ora",
            joinColumns = @JoinColumn(name = "ora_id"),
            inverseJoinColumns = @JoinColumn(name = "materie_id")
    )
    private List<Materie> materii = new ArrayList<>();
}
