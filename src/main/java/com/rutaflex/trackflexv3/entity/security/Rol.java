package com.rutaflex.trackflexv3.entity.security;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ROL")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String nombre;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private List<GrantedPermission> permisos;
}
