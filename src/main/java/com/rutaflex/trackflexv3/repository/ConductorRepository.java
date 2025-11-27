package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    boolean existsByDni(String dni);
    boolean existsByLicencia(String licencia);

    // RF12: Conductores activos
    List<Conductor> findByEstado(String estado);
}