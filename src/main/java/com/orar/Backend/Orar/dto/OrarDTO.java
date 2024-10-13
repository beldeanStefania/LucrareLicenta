package com.orar.Backend.Orar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class OrarDTO {
    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", example = "12:30:00")
    private LocalTime intervalOrarInceput;

    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", example = "12:30:00")
    private LocalTime intervalOrarSfarsit;

    private String ziua;
    private Integer oraId;
    private Integer grupaId;
}
