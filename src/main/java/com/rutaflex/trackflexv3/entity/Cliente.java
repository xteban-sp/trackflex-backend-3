package com.rutaflex.trackflexv3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruc;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    private String contacto;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String estado = "A";

    // Relación: un cliente puede tener múltiples almacenes (vía tabla intermedia)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClienteAlmacen> almacenesAsociados = new ArrayList<>();
}