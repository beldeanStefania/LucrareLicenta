package com.orar.Backend.Orar.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class OrarDTO {
    private LocalTime oraInceput;
    private LocalTime oraSfarsit;
    private String ziua;
    private String materia;
}
