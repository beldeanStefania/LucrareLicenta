package com.orar.Backend.Orar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Rol {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    private int id;

    private String name;

    @OneToMany
    private List<User> users;
}
