package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByRuc(String ruc);
    boolean existsByRazonSocial(String razonSocial);
    Optional<Cliente> findByRucAndEstado(String ruc, String estado);
    List<Cliente> findByEstado(String estado);
}