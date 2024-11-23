package com.orar.Backend.Orar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {
    private String nume;
    private String prenume;
    private String grupa;
    private Integer an;
    private String username; // Câmp pentru user
    private String password; // Câmp pentru parola user-ului
}
