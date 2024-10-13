package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToMany(mappedBy = "sala", cascade = ALL)
    @JsonManagedReference
    private List<Ora> ora;
}
