package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.orar.Backend.Orar.enums.Tip;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class CurriculumEntry {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "specializare_id", nullable = false)
    @JsonBackReference
    private Specializare specializare;

    @ManyToOne
    @JoinColumn(name = "materie_id", nullable = false)
    @JsonBackReference
    private Materie materie;

    private Integer an;
    private Integer semestru;

    @Enumerated(EnumType.STRING)
    private Tip tip;
}
