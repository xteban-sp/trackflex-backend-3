// src/main/java/com/rutaflex/trackflexv3/service/general/service/VehiculoService.java
package com.rutaflex.trackflexv3.service.general.service;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.service.base.GenericService;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoService extends GenericService<VehiculoDTO> {


    List<VehiculoDTO> findByIds(List<Long> ids);

    List<VehiculoDTO> findDisponibles(LocalDate fecha, Long nivelId);
}