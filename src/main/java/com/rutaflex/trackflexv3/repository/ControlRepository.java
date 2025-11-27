package com.rutaflex.trackflexv3.repository;

import com.rutaflex.trackflexv3.entity.Control;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ControlRepository extends JpaRepository<Control, Long> {
    List<Control> findByTablaAfectada(String tabla);
    List<Control> findByIdRegistro(Long idRegistro);
    List<Control> findByTipoAccion(String tipoAccion);
    List<Control> findByFechaAccionBetween(LocalDateTime inicio, LocalDateTime fin);
}