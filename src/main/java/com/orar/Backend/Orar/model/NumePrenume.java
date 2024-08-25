package com.orar.Backend.Orar.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record NumePrenume(String nume, String prenume) {
}
