package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicioDTO {
    private Long id;
    private String codigoServicio;
    private Long vehiculoId;
    private Long clienteId;
    private Long usuarioId;
    private Long estadoId;
    private Long rutaId;
    private String estadoRegistro;
    private String observacion;
    private Double volumen;
    private Double totalFlete;
    private Double gastos;
    private LocalDate fecha;
}