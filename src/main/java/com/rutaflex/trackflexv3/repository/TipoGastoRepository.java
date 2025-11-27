package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoGasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoGastoRepository extends JpaRepository<TipoGasto, Long> {

    boolean existsByDescripcion(String descripcion);

    List<TipoGasto> findByEstado(String estado);

    // Opcional: buscar por descripción exacta y estado
    List<TipoGasto> findByDescripcionAndEstado(String descripcion, String estado);
}