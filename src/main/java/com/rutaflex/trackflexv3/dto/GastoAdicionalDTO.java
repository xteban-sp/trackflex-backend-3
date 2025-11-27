package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GastoAdicionalDTO {

    private Long id;
    private Long servicioId;           // Servicio al que pertenece
    private Long tipoGastoId;          // Ej. "Gasolina", "Peaje", "Arreglo"
    private BigDecimal monto;              // Monto del gasto (precisión 10,2)
    private String descripcion;        // Detalle del gasto
    private LocalDate fecha;           // Fecha en que se incurrió el gasto (por defecto: hoy)
}