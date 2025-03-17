package com.orar.Backend.Orar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orar.Backend.Orar.model.ZI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrarDTO {
    private String grupa;
    private String semigrupa;
    private int oraInceput;
    private int oraSfarsit;
    private String zi;
    private Integer repartizareProfId;
    private Integer salaId;
    private String tip; 
    private String materie; 
    private Integer profesorId; 
    private String frecventa; 
}