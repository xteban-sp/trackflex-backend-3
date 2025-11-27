package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    // RF27: Incidencias por entrega
    List<Incidencia> findByEntregaId(Long entregaId);

    // RF30: Notificación → ya cubierto al crear
}