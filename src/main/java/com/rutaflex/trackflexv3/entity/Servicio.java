package com.rutaflex.trackflexv3.entity;

import com.rutaflex.trackflexv3.entity.security.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "servicio",
        uniqueConstraints = @UniqueConstraint(columnNames = "codigo_servicio"))
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_servicio", nullable = false, unique = true, length = 20)
    private String codigoServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idvehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestado", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idruta", nullable = false)
    private Ruta ruta;

    @Column(name="estado", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estadoRegistro = "A";

    private String observacion;
    private BigDecimal volumen;
    private BigDecimal totalFlete;
    private BigDecimal gastos;
    private LocalDate fecha;
}