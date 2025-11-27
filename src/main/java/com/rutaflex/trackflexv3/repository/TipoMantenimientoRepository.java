package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoMantenimientoRepository extends JpaRepository<TipoMantenimiento, Long> {
    boolean existsByDescripcion(String descripcion);
    List<TipoMantenimiento> findByEstado(String estado);
    List<TipoMantenimiento> findByDescripcion(String descripcion);
}