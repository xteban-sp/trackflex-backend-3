package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CanalDTO {
    private Long id;
    private String nombre;
    private String estado;
}