package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "control")
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tabla_afectada", nullable = false)
    private String tablaAfectada;

    @Column(name = "id_registro", nullable = false)
    private Long idRegistro;

    @Column(name = "tipo_accion", nullable = false)
    private String tipoAccion; // 'INSERT', 'UPDATE', 'DELETE'

    @Column(name = "datos_anteriores", columnDefinition = "CLOB")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "CLOB")
    private String datosNuevos;

    @Column(name = "operacion_inversa", columnDefinition = "CLOB")
    private String operacionInversa;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDate fechaAccion = LocalDate.now();
}