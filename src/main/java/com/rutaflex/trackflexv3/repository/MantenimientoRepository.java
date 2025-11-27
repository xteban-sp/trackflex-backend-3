package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByVehiculoId(Long vehiculoId);
    // RF01, RF03, RF62–RF68: Verifica si existe mantenimiento activo en una fecha para un vehículo
    boolean existsByVehiculoIdAndFechaAndEstado(Long vehiculoId, LocalDate fecha, String estado);

    // Opcional: listar mantenimientos por vehículo y fecha
    List<Mantenimiento> findByVehiculoIdAndFecha(Long vehiculoId, LocalDate fecha);
    // RF66: Vehículos en mantenimiento → se filtra por estado del vehículo, no del mantenimiento
}