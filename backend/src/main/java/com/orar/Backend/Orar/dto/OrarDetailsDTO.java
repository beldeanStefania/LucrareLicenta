package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrarDetailsDTO {
    private String zi;
    private String formatia;
    private int oraInceput;
    private int oraSfarsit;
    private String grupa;
    private String sala;
    private String tipul; // Tipul din RepartizareProf
    private String disciplina; // Numele materiei
    private String cadruDidactic; // Numele profesorului
}
