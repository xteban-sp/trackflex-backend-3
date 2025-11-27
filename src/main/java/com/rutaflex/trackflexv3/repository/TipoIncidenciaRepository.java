package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Long> {
    boolean existsByDescripcion(String descripcion);
    List<TipoIncidencia> findByEstado(String estado);

    List<TipoIncidencia> findByDescripcion(String descripcion);
}