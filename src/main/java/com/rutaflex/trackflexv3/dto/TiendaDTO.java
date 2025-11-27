package com.rutaflex.trackflexv3.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TiendaDTO {
    private Long id;
    private String direccion;
    private String nombre;
    private String codDestinatario;
    private String contacto;
    private Long distritoId;
    private String estado;
}