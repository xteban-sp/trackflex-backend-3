package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v WHERE v.nivel.m3Min <= :capacidad AND v.nivel.m3Max >= :capacidad AND v.estado = 'A'")
    List<Vehiculo> findByCapacidad(@Param("capacidad") Double capacidad);

    @Query("""
        SELECT v FROM Vehiculo v
        WHERE v.estado = 'A'
        AND (:nivelId IS NULL OR v.nivel.id = :nivelId)
        AND v.id NOT IN (
            SELECT s.vehiculo.id 
            FROM Asignacion a 
            JOIN a.servicio s
            WHERE a.estado = 'A' 
            AND a.fechaInicio <= :fecha 
            AND a.fechaFin >= :fecha
        )
        AND v.id NOT IN (
            SELECT m.vehiculo.id 
            FROM Mantenimiento m 
            WHERE m.estado = 'A' 
            AND m.fecha = :fecha
        )
        """)
    List<Vehiculo> findDisponibles(@Param("fecha") LocalDate fecha, @Param("nivelId") Long nivelId);
    List<Vehiculo> findByEstado(String estado);
    List<Vehiculo> findByIdInAndEstado(List<Long> ids, String estado);
    List<Vehiculo> findByIdIn(List<Long> ids);
}