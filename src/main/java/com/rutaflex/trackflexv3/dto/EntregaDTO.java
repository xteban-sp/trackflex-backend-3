package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntregaDTO {
    private Long id;
    private String codEntrega;
    private Double volumenM3;
    private String vhInicio;
    private String vhFinal;
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegada;
    private String observacionCliente;
    private Long psexId;
    private Long canalId;
    private Long servicioId;
    private Long tiendaId;
    private Long estadoId;
    private String estadoRegistro;
}