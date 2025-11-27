package com.rutaflex.trackflexv3.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "granted_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrantedPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacion_id", nullable = false)
    private Operacion operacion;
}
