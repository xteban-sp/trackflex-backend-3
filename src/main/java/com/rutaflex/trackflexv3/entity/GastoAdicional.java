package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "gasto_adicional")
public class GastoAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtipo_gasto", nullable = false)
    private TipoGasto tipoGasto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha = LocalDate.now();
}