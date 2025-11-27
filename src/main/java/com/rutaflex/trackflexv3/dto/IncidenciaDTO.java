package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IncidenciaDTO {
    private Long id;
    private Long entregaId;
    private Long tipoIncidenciaId;
    private Long estadoId;
    private String descripcion;
    private LocalDate fechaRegistro;
    private LocalDate fechaResolucion;
    private String observaciones;
    private String nombreImagen;
    private String rutaImagen;
    private String ubicacion;
    private String estadoRegistro;
}