package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    @OneToMany(mappedBy = "sala", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Orar> orar;
}
