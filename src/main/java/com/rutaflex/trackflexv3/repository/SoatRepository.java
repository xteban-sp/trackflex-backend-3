package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Soat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SoatRepository extends JpaRepository<Soat, Long> {
    boolean existsByNroPoliza(String nroPoliza);
    Optional<Soat> findByNroPoliza(String nroPoliza);

    List<Soat> findByVehiculoId(Long vehiculoId);
    List<Soat> findByEmpresaSoatId(Long empresaSoatId);

    List<Soat> findByTipoSoatId(Long tipoSoatId);
    List<Soat> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);
    List<Soat> findByEstado(String estado);
}