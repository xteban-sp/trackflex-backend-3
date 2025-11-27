package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", length = 7, nullable = false, unique = true)
    private String placa;

    @Column(name = "marca", length = 30, nullable = false)
    private String marca;

    @Column(name = "modelo", length = 30)
    private String modelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idnivel", nullable = false)
    private Nivel nivel; // Relación con la tabla 'nivel'

    @Column(name = "ESTADO", length = 1, nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}