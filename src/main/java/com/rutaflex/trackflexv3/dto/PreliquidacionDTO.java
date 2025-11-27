package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PreliquidacionDTO {

    private Long id;
    private Long servicioId;           // Referencia al servicio
    private Double totalFlete;         // Monto calculado por tarifario
    private Double otrosCargos;        // Gastos adicionales (RF43)
    private String tarifa;             // Descripción o nombre de la tarifa aplicada (ej. "RUTA - P1")
    private LocalDate fecha;           // Fecha de la preliquidación
}