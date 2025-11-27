package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoUrgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoUrgenciaRepository extends JpaRepository<TipoUrgencia, Long> {
    boolean existsByDescripcion(String descripcion);
    List<TipoUrgencia> findByEstado(String estado);
    List<TipoUrgencia> findByDescripcion(String descripcion);
}