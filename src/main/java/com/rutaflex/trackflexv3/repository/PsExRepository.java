package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.PsEx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsExRepository extends JpaRepository<PsEx, Long> {
    List<PsEx> findByAlmacenId(Long almacenId);
    List<PsEx> findByEstado(String estado);
}