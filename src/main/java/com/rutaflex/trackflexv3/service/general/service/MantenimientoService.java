package com.rutaflex.trackflexv3.service.general.service;

import com.rutaflex.trackflexv3.dto.MantenimientoDTO;
import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.base.GenericService;

import java.time.LocalDate;
import java.util.List;

public interface MantenimientoService extends GenericService<MantenimientoDTO> {


    void cancelarMantenimiento(Long id) throws Exception;

    // RF01, RF03: Verificar disponibilidad
    boolean isVehiculoDisponibleEnFecha(Long vehiculoId, LocalDate fecha) throws Exception;

    // RF01, RF03, RF05: Listar vehículos disponibles en una fecha
    List<VehiculoDTO> findVehiculosDisponiblesEnFecha(LocalDate fecha) throws Exception;
}