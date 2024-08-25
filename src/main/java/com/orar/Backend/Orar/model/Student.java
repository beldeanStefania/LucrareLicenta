package com.orar.Backend.Orar.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private String prenume;
    private Integer grupa;
    private Integer an;
}
