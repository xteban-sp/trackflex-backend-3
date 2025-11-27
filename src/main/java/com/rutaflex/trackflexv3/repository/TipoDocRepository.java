package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.TipoDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoDocRepository extends JpaRepository<TipoDoc, Long> {
    boolean existsByDescripcion(String descripcion);
    List<TipoDoc> findByEstado(String estado);
    List<TipoDoc> findByDescripcion(String descripcion);
}