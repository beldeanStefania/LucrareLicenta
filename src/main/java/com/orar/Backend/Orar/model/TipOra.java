package com.orar.Backend.Orar.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record TipOra(String laborator, String seminar, String curs) {
}
