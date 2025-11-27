package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "incidencia")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identrega", nullable = false)
    private Entrega entrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtipo_incidencia", nullable = false)
    private TipoIncidencia tipoIncidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestado", nullable = false)
    private Estado estado;

    private String descripcion;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    @Column(name = "fecha_resolucion")
    private LocalDate fechaResolucion;

    private String observaciones;
    private String nombreImagen;
    private String rutaImagen;
    private String ubicacion;

    @Column(name ="estado",nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estadoRegistro = "A";
}