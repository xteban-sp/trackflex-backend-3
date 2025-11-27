package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TarifarioDTO {

    private Long id;
    private NivelDTO nivel;
    private RutaDTO ruta;
    private String tipo;
    private Double tarifa;
    private String estado;
}