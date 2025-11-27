package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_entrega", nullable = false)
    private String codEntrega;

    @Column(name = "volumen_M3")
    private BigDecimal volumenM3;
    private String vhInicio;
    private String vhFinal;
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegada;

    @Column(name = "observacion_cliente")
    private String observacionCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpsex", nullable = false)
    private PsEx psex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcanal", nullable = false)
    private Canal canal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtienda", nullable = false)
    private Tienda tienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestado", nullable = false)
    private Estado estado;

    @Column(name = "estado", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estadoRegistro = "A";
}