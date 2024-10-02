package com.orar.Backend.Orar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class OrarDTO {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime oraInceput;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime oraSfarsit;
    private String ziua;
    private List<OraDTO> ore;
}
