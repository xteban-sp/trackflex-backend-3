package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Preliquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreliquidacionRepository extends JpaRepository<Preliquidacion, Long> {

    Optional<Preliquidacion> findByServicioId(Long servicioId);
}