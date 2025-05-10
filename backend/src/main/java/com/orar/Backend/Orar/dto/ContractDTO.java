package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.enums.Tip;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {
    private String cod;
    private String nume;
    private int credite;
    private int semestru;
    private Tip tip;
    private boolean selected;

    public ContractDTO(String cod, String nume, int credite, int semestru, Tip tip, boolean selected) {
        this.cod = cod;
        this.nume = nume;
        this.credite = credite;
        this.semestru = semestru;
        this.tip = tip;
        this.selected = selected;
    }
}
