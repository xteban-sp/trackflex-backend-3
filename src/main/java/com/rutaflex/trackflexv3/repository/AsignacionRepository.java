package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    // RF12, RF13: Asignación por servicio
    List<Asignacion> findByServicioId(Long servicioId);

    // RF14: Asignar personal → ya cubierto con save

    // RF16: Notificar conductor → buscar por conductorId
    List<Asignacion> findByConductorId(Long conductorId);

    // RF68: Reactivar vehículo → no directo, pero útil
}