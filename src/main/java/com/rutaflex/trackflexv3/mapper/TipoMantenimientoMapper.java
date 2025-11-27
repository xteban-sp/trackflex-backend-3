package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TipoMantenimientoDTO;
import com.rutaflex.trackflexv3.entity.TipoMantenimiento;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoMantenimientoMapper extends BaseMapper<TipoMantenimiento, TipoMantenimientoDTO> {

}