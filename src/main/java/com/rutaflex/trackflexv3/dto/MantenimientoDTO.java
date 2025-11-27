package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MantenimientoDTO {
    private Long id;
    private Long vehiculoId;
    private Long tipoMantenimientoId;
    private Long tipoUrgenciaId;
    private LocalDate fecha;
    private String diagnostico;
    private String estado;
}