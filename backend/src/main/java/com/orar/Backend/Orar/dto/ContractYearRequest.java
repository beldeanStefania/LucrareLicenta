package com.orar.Backend.Orar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContractYearRequest {
    private String studentCod;
    private int anContract;
    private List<String> coduriMaterii;
}
