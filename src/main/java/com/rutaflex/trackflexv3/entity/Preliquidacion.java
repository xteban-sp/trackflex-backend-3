package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "preliquidacion")
public class Preliquidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;

    @Column(name = "total_flete", nullable = false)
    private BigDecimal totalFlete;

    private BigDecimal otrosCargos;
    private String tarifa;

    @Column(nullable = false)
    private LocalDate fecha;
}