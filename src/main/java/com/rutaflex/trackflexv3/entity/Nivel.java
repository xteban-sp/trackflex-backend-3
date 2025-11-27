package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "nivel")
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(name = "m3_min", nullable = false)
    private BigDecimal m3Min;

    @Column(name = "m3_max", nullable = false)
    private BigDecimal m3Max;

    // Constraint: m3_min <= m3_max → se valida en la BD o en el servicio
}