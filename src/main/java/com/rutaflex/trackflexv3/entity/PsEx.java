package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "psex")
public class PsEx {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen", nullable = false)
    private Almacen almacen;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}