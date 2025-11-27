package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    boolean existsByCodEntrega(String codEntrega);
    Optional<Entrega> findByCodEntrega(String codEntrega);

    // RF17, RF23: Entregas pendientes
    @Query("SELECT e FROM Entrega e JOIN e.estado est WHERE est.descripcion IN ('Pendiente', 'Programado') ORDER BY e.horaSalida")
    List<Entrega> findEntregasPendientes();

    // RF38: Entregas finalizadas
    @Query("SELECT e FROM Entrega e JOIN e.estado est WHERE est.descripcion = 'Entregado'")
    List<Entrega> findEntregasFinalizadas();

    // RF25, RF33, RF37: Actualizar estado → se hace en servicio, pero filtro útil
    List<Entrega> findByEstadoId(Long estadoId);
    List<Entrega> findByServicioId(Long servicioId);


}