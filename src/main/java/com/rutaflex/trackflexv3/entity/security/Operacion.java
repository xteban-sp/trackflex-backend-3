package com.rutaflex.trackflexv3.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String httpMethod;

    @Column(nullable = false, length = 100)
    private String path;

    @Column(name = "permit_all", nullable = false)
    private Integer permitAll;

    @ManyToOne(optional = false)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}
