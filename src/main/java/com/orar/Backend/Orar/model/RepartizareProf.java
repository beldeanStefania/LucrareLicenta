package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RepartizareProf {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private TIP tip;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = LAZY)
    @JsonBackReference
    @JoinColumn(name = "materie_id", nullable = false)
    private Materie materie;

    @OneToMany(mappedBy = "repartizareProf", cascade = ALL)
    @JsonManagedReference
    private List<Orar> orar = new ArrayList<>();
}
