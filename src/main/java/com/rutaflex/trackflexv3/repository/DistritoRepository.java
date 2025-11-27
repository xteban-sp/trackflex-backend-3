package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito, Long> {
    boolean existsByNombre(String nombre);
    List<Distrito> findByNombre(String nombre);
    List<Distrito> findByEstado(String estado);
}