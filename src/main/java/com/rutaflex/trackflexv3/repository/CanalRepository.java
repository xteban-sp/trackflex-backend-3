package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Canal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanalRepository extends JpaRepository<Canal, Long> {
    boolean existsByNombre(String nombre);
    List<Canal> findByEstado(String estado);
    List<Canal> findByNombre(String nombre);
}