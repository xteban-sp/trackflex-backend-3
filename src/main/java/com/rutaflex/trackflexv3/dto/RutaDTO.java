package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RutaDTO {
    private Long id;
    private String nombre;
    private String estado;
}