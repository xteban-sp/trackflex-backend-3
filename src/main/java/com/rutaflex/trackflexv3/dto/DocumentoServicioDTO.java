package com.rutaflex.trackflexv3.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentoServicioDTO {
    private Long id;
    private Long servicioId;
    private String tipoDocumento;
    private String nombreArchivo;
    private String rutaArchivo;
    private LocalDate fechaSubida;
}