package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.GastoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoAdicionalRepository extends JpaRepository<GastoAdicional, Long> {

    List<GastoAdicional> findByServicioId(Long servicioId);
}