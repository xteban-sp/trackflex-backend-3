package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    boolean existsByCodDestinatario(String codDestinatario);
    boolean existsByNombre(String nombre);
    Optional<Tienda> findByCodDestinatarioAndEstado(String codDestinatario, String estado);
    List<Tienda> findByDistritoId(Long iddistrito);
    List<Tienda> findByEstado(String estado);
}