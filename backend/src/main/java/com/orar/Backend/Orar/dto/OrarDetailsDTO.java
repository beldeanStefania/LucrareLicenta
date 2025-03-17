package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrarDetailsDTO {
    private String zi;
    private String formatia;
    private int oraInceput;
    private int oraSfarsit;
    private String grupa;
    private String sala;
    private String tipul; 
    private String disciplina; 
    private String cadruDidactic; 
    private String frecventa;
}