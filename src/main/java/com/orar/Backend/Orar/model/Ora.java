package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;
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
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TipOra tip;

    @OneToMany(mappedBy = "ora", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrarOra> orarOre = new ArrayList<>();

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

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime intervalOrarSfarsit;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime intervalOrarInceput;

}
