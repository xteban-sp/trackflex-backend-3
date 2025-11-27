package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmpresaSoatDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String estado;
}