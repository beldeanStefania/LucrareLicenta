package com.orar.Backend.Orar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Materie {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private Integer semestru;

    @ManyToMany(mappedBy = "materii")
    private List<Ora> ore = new ArrayList<>();
}
