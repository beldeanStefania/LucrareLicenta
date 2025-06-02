package com.orar.Backend.Orar.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {
    private String studentCod;
    private String title;
    private String description;
    private String deadline;
    private Boolean done;
}
