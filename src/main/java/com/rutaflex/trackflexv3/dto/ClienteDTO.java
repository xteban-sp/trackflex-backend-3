package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteDTO {
    private Long id;
    private String ruc;
    private String razonSocial;
    private String contacto;
    private String estado;
    private List<Long> almacenesIds; // IDs de almacenes asociados (para requests)
}