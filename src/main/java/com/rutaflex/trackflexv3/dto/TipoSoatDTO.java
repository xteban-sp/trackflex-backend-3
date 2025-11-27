package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoSoatDTO {
    private Long id;
    private String descripcion;
    private String estado;
}