package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.model.NumePrenume;
import com.orar.Backend.Orar.model.TipOra;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OraDTO {
    private TipOra tip;
    private String sala;
    private String profesor;
    private String materie;
}
