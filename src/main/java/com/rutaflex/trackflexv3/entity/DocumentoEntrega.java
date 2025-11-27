package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "documento_entrega")
public class DocumentoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identrega", nullable = false)
    private Entrega entrega;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida = LocalDate.now();
}