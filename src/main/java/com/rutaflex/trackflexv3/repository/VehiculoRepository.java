package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v WHERE v.nivel.m3Min <= :capacidad AND v.nivel.m3Max >= :capacidad AND v.estado = 'A'")
    List<Vehiculo> findByCapacidad(@Param("capacidad") Double capacidad);

    List<Vehiculo> findByEstado(String estado);

    List<Vehiculo> findByIdIn(List<Long> ids);
}