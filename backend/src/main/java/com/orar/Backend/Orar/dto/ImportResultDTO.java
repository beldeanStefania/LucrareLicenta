package com.orar.Backend.Orar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultDTO {
    private int row;
    private boolean success;
    private String message;
}

