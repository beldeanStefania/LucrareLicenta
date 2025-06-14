package com.orar.Backend.Orar.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String studentCod;
    private int anContract;

    @CreationTimestamp
    private LocalDateTime dataGenerare;

    @ElementCollection
    @CollectionTable(name = "contract_materii",
            joinColumns = @JoinColumn(name = "contract_id"))
    @Column(name = "materie_cod")
    private List<String> coduriMaterii;

    public Contract(String studentCod, int anContract) {
        this.studentCod  = studentCod;
        this.anContract  = anContract;
    }
}
