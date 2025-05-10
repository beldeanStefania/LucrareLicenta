package com.orar.Backend.Orar.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class StudentDTO {
    private String cod;
    private String nume;
    private String prenume;
    private String grupa;
    private Integer an;
    private String username;
    private String password;
    private String specializare;
}
