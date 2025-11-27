package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.EmpresaSoat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaSoatRepository extends JpaRepository<EmpresaSoat, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByRuc(String ruc);
    List<EmpresaSoat> findByEstado(String estado);
    List<EmpresaSoat> findByNombre(String nombre);
    List<EmpresaSoat> findByRuc(String ruc);
}