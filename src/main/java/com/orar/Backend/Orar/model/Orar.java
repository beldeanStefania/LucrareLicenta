package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Orar {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "orar", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ora> ora;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime oraInceput;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime oraSfarsit;
    private String ziua;
}
