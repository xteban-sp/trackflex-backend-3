package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.DocumentoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoEntregaRepository extends JpaRepository<DocumentoEntrega, Long> {

    // RF19, RF35: Documentos por entrega
    List<DocumentoEntrega> findByEntregaId(Long entregaId);

    // RF46, RF47: Visualizar/descargar → ya cubierto con findByEntregaId
}