package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoIncidenciaDTO {
    private Long id;
    private String descripcion;
    private String estado;
}