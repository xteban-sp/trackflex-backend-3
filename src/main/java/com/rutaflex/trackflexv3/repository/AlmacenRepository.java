package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    boolean existsByNombre(String nombre);
    List<Almacen> findByEstado(String estado);
    List<Almacen> findByNombre(String nombre);
}