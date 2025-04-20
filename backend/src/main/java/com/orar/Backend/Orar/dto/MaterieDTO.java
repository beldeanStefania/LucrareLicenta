package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MaterieDTO {
    private String nume;
    private Integer semestru;
    private String cod;
    private Integer credite;
}
