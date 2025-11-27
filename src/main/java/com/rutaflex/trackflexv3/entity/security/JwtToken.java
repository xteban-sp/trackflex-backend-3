package com.rutaflex.trackflexv3.entity.security;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String token;

    @Column(name = "is_valid", nullable = false)
    private Integer isValid;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}