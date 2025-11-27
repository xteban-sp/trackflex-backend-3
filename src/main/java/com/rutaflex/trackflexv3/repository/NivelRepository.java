package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NivelRepository extends JpaRepository<Nivel, Long> {
    boolean existsByCodigo(String codigo);

    /**
     * Busca un nivel por su código
     */
    Optional<Nivel> findByCodigo(String codigo);

    /**
     * Busca niveles cuyo rango de m³ incluya una capacidad dada
     * Útil para filtrar vehículos por capacidad (RF03)
     */
    @Query("SELECT n FROM Nivel n WHERE n.m3Min <= :capacidad AND n.m3Max >= :capacidad")
    List<Nivel> findByCapacidad(@Param("capacidad") Double capacidad);
}

