package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradeDTO {
    private String disciplina; // Numele materiei
    private Double nota;       // Nota primită
}