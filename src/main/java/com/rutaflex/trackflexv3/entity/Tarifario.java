package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "tarifario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idnivel", "idruta", "tipo"}))
public class Tarifario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idnivel", nullable = false)
    private Nivel nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idruta", nullable = false)
    private Ruta ruta;

    @Column(nullable = false)
    private String tipo; // 'RUTA', 'FALSO_FLETE', 'PROVINCIA'

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}