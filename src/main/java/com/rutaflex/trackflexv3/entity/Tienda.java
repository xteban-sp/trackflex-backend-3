package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "tienda")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "coddestinatario")
    private String codDestinatario;

    private String contacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddistrito", nullable = false)
    private Distrito distrito;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";
}