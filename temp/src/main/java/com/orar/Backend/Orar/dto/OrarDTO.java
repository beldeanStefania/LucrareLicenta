package com.orar.Backend.Orar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orar.Backend.Orar.model.ZI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class OrarDTO {
    private String grupa;
    private int oraInceput;
    private int oraSfarsit;
    private String zi;
    private int repartizareProfId;
    private int salaId;
}
