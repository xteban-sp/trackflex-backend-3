package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ControlDTO {
    private Long id;
    private String tablaAfectada;
    private Long idRegistro;
    private String tipoAccion;
    private String datosAnteriores;
    private String datosNuevos;
    private String operacionInversa;
    private LocalDate fechaAccion;
}