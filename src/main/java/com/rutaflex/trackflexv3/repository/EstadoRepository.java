package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    List<Estado> findByTipo(String tipo); // ej. "entrega", "servicio"
    List<Estado> findByEstado(String estado);
}