package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.ClienteAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteAlmacenRepository extends JpaRepository<ClienteAlmacen, Long> {
    List<ClienteAlmacen> findByClienteId(Long clienteId);
    List<ClienteAlmacen> findByAlmacenId(Long almacenId);
}