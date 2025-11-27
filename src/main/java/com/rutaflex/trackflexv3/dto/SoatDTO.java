package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SoatDTO {
    private Long id;
    private Long empresaSoatId;
    private Long vehiculoId;
    private Long tipoSoatId;
    private String nroPoliza;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}