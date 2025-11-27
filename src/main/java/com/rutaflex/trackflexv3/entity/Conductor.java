package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "conductor")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private String dni;
    private String licencia;
    private String telefono;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}