package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NivelDTO {
    private Long id;
    private String codigo;
    private Double m3Min;
    private Double m3Max;
}