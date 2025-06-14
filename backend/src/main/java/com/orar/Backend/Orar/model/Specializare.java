package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Specializare {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String specializare;

    @OneToMany(mappedBy = "specializare", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<CurriculumEntry> curriculum;

    @OneToMany(mappedBy = "specializare", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Student> studenti;
}
