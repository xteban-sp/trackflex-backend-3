package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.DocumentoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoServicioRepository extends JpaRepository<DocumentoServicio, Long> {

    List<DocumentoServicio> findByServicioId(Long servicioId);
}