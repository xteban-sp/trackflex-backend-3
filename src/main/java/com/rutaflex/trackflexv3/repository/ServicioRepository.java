package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // RF02, RF07: Servicios por fecha
    List<Servicio> findByFecha(LocalDate fecha);

    // RF06, RF44, RF50: Todos los servicios
    List<Servicio> findByOrderByFechaDesc();

    // RF11: Servicios "Sin asignar" → estado = 'S' (asume que usas código de estado)
    List<Servicio> findByEstadoDescripcion(String estadoDescripcion);

    // RF54: Servicios con estado "Validado"
    @Query("SELECT s FROM Servicio s JOIN s.estado e WHERE e.descripcion = 'Validado'")
    List<Servicio> findByEstadoValidado();

    // RF01: Vehículos usados en un servicio → esta lógica va en servicio, no aquí
}