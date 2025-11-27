package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionDTO {
    private Long id;
    private Long servicioId;
    private Long conductorId;
    private Long personal1Id;
    private Long personal2Id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}