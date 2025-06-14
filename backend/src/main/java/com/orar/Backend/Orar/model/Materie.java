package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Materie {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String nume;
    private Integer semestru;
    private Integer an;

    @Column(unique = true)
    private String cod;
    private Integer credite;

    @OneToMany(mappedBy = "materie", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CatalogStudentMaterie> catalogStudentMaterie;

    @OneToMany(mappedBy = "materie", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RepartizareProf> repartizareProfs;
}
