package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cladire {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String nume;
    private String adresa;

    @OneToMany(mappedBy = "cladire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Sala> sala;
}
