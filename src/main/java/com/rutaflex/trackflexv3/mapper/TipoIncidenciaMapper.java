package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TipoIncidenciaDTO;
import com.rutaflex.trackflexv3.entity.TipoIncidencia;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoIncidenciaMapper extends BaseMapper<TipoIncidencia, TipoIncidenciaDTO> {
}