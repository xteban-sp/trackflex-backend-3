package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Tarifario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarifarioRepository extends JpaRepository<Tarifario, Long> {

    // RF56: Tarifa por nivel, ruta y tipo
    List<Tarifario> findByNivelIdAndRutaIdAndTipo(Long idNivel, Long idRuta, String tipo);
}