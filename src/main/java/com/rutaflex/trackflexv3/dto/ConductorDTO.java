package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConductorDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String licencia;
    private String telefono;
    private String estado;
}