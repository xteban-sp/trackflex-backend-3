package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    boolean existsByNombre(String nombre);
    List<Ruta> findByEstado(String estado);
    Optional<Ruta> findByNombre(String nombre);
}