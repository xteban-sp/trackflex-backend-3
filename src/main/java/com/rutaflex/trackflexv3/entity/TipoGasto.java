package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "tipo_gasto", uniqueConstraints = @UniqueConstraint(columnNames = "descripcion"))
public class TipoGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}