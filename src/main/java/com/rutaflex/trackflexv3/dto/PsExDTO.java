package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PsExDTO {
    private Long id;
    private String nombre;
    private Long almacenId;
    private String estado;
}