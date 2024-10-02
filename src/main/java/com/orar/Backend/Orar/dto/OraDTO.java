package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.model.NumePrenume;
import com.orar.Backend.Orar.model.TipOra;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OraDTO {
    private TipOra tip;
    private Integer salaId;
    private Integer profesorId;
    private Integer materieId;
    private Integer orarId;
}
