package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterieDTO {
    private String nume;
    private Integer semestru;
    private String cod;
    private Integer credite;
}
