package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentoEntregaDTO {
    private Long id;
    private Long entregaId;
    private String tipoDocumento;
    private String nombreArchivo;
    private String rutaArchivo;
    private LocalDate fechaSubida;
}