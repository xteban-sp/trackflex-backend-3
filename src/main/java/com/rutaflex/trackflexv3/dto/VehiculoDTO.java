package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehiculoDTO {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private NivelDTO nivel;
    private String estado;
}