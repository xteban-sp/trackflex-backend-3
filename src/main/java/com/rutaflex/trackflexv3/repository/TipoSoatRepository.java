package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoSoat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoSoatRepository extends JpaRepository<TipoSoat, Long> {
    boolean existsByDescripcion(String descripcion);
    List<TipoSoat> findByEstado(String estado);
    List<TipoSoat> findByDescripcion(String descripcion);
}