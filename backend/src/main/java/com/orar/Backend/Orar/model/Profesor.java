package com.orar.Backend.Orar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Profesor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer Id;

    private String nume;
    private String prenume;

    @OneToMany(mappedBy = "profesor", cascade = ALL)
    @JsonManagedReference
    private List<RepartizareProf> repartizareProfs;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
